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

//    private final BrokenDgPool brokens = new DecoderBrokenPool();

    private Class<? extends ChainDecoder> decoderClass;

    private ChainDecoder getChainService(){
        return Optional.ofNullable(decoderClass).map(clazz -> {
            Iterator<? extends ChainDecoder> iterable = ServiceLoader.load(this.decoderClass).iterator();
            ChainDecoder next = null;
            while (iterable.hasNext()){
                next = (ChainDecoder) iterable.next();
            }
            return next;
        }).orElse(null);
    }

    public DgDecoder(){

    }

    public DgDecoder(Class<? extends ChainDecoder> decoderClass){
        this.decoderClass = decoderClass;
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
        byte[] body = Arrays.copyOfRange(payload, 1 + 2, Math.min(1 + 2 + CommonMethod.toShort(length), payload.length));

        result.add(new Dg(id, afn, length, body));
    }


    public Class<?> getDecoderClass() {
        return decoderClass;
    }

    public void setDecoderClass(Class<? extends ChainDecoder> decoderClass) {
        this.decoderClass = decoderClass;
    }
}
