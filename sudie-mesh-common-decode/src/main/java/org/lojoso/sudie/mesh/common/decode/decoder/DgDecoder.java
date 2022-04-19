package org.lojoso.sudie.mesh.common.decode.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.lojoso.sudie.mesh.common.decode.utils.DgTools;
import org.lojoso.sudie.mesh.common.model.CommonMethod;
import org.lojoso.sudie.mesh.common.model.Dg;

import java.util.*;

public class DgDecoder extends ByteToMessageDecoder {

//    private final BrokenDgPool brokens = new DecoderBrokenPool();

    private ChainDecoder getChainService(){
        Iterator<ChainDecoder> iterable = ServiceLoader.load(ChainDecoder.class).iterator();
        ChainDecoder next = null;
        while (iterable.hasNext()){
            next = iterable.next();
        }
        return next;
    }

    public void pubDecode(byte[] bytes){
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
    private void normalDatagram(byte[] payload, List<Dg> result, ChannelId id){

        // 1 == AFN.length
        byte[] afn = Arrays.copyOfRange(payload, 0, 1);
        // 2 == Length.length
        byte[] length = Arrays.copyOfRange(payload, 1, 1 + 2);
        // 1 == crc.length
        byte[] body = Arrays.copyOfRange(payload, 1 + 2, Math.min(1 + 2 + DgTools.toShort(length), payload.length));

        result.add(new Dg(id, afn, length, body));
    }


}
