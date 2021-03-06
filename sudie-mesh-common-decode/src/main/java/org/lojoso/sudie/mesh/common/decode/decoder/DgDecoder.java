package org.lojoso.sudie.mesh.common.decode.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.lojoso.sudie.mesh.common.model.CommonMethod;
import org.lojoso.sudie.mesh.common.model.Dg;

import java.util.*;

public class DgDecoder extends ByteToMessageDecoder {

    private ChainDecoder.DecoderType type;

    private ChainDecoder getChainService() {
        Iterator<? extends ChainDecoder> iterable = ServiceLoader.load(ChainDecoder.class).iterator();
        ChainDecoder next = null;
        while (iterable.hasNext()) {
            next = (ChainDecoder) iterable.next().selected(this.type);
            if(Objects.nonNull(next)){
                break;
            }
        }
        return next;
    }

    public DgDecoder() {

    }

    public DgDecoder(ChainDecoder.DecoderType type) {
        this.type = type;
    }

    public void pubDecode(byte[] bytes) {
        this.analysis(bytes, null, new ArrayList<>());
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> list) {
        byte[] payload = ByteBufUtil.getBytes(in);
        List<Dg> dgs = analysis(payload, ctx.channel().id(), new ArrayList<>());

        // 传递list
        list.add(Optional.ofNullable(getChainService()).<List<? extends Dg>>map(svc -> svc.chainDecoder(dgs)).orElse(dgs));
        // 跳过已读
        in.skipBytes(in.readableBytes());
    }


    private List<Dg> analysis(byte[] srcPayload, ChannelId id, List<Dg> result) {
        return scatterPayload(srcPayload, result, id);
    }


    private List<Dg> scatterPayload(byte[] payload, List<Dg> result, ChannelId id) {
        this.normalDatagram(payload, result, id);
        return result;
    }


    // 正常判断
    private void normalDatagram(byte[] payload, List<Dg> result, ChannelId id) {

        // 1 == AFN.length
        byte[] afn = Arrays.copyOfRange(payload, 0, 1);
        // 2 == Length.length
        byte[] length = Arrays.copyOfRange(payload, 1, 1 + 2);
        // 1 == crc.length
        byte[] body = Arrays.copyOfRange(payload, 1 + 2, Math.min(1 + 2 + CommonMethod.toShort(length), payload.length));

        result.add(new Dg(id, afn, length, body));
    }


    public ChainDecoder.DecoderType getType() {
        return type;
    }

    public void setType(ChainDecoder.DecoderType type) {
        this.type = type;
    }
}
