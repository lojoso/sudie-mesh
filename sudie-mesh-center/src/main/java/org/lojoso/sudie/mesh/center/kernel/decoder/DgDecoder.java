package org.lojoso.sudie.mesh.center.kernel.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.lojoso.sudie.mesh.center.kernel.model.Dg;
import org.lojoso.sudie.mesh.center.utils.DgTools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DgDecoder extends ByteToMessageDecoder {


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> list) {
        byte[] payload = ByteBufUtil.getBytes(in);
        List<Dg> dgs = analysis(payload, ctx.channel().id(), new ArrayList<>());
        // 传递list
        list.add(dgs);
        // 跳过已读
        in.skipBytes(in.readableBytes());
    }


    private List<Dg> analysis(byte[] srcPayload, ChannelId id, List<Dg> result) {
        return scatterPayload(srcPayload, result, id);
    }


    private List<Dg> scatterPayload(byte[] payload, List<Dg> result, ChannelId id) {
        for (int i = 0; i < payload.length; i++) {
            // 查找报文头
            if (Arrays.equals(DgTools.HEAD, Arrays.copyOfRange(payload, i, i + 1))) {
                if (i != 0 && result.size() == 0) {
                    // 前报文缺失
                    result.add(new Dg(id, Arrays.copyOfRange(payload, 0, i)));
                }
                i = generateDatagram(payload, result, i, id)-1;
            }
        }
        return result;
    }

    private int generateDatagram(byte[] payload, List<Dg> result, int i, ChannelId id) {
        // 带有报文头，是否缺失尾部
        return Optional.of(payload).filter(d -> d.length >= i + 1 + 1 + 2 + 1).map(d -> this.normalDatagram(payload,result,i,id))
                .orElseGet(() -> this.brokenDatagram(payload, result, i, id));
    }

    // 缺失尾部
    private int brokenDatagram(byte[] payload, List<Dg> result, int i, ChannelId id){
        Dg data = new Dg();
        data.setId(id);
        data.setBroken(true);
        Optional.of(payload.length).filter(l -> l >= i + 1).ifPresent(l -> data.setHead(Arrays.copyOfRange(payload, i, i + 1)));
        Optional.of(payload.length).filter(l -> l >= i + 2).ifPresent(l -> data.setAfn(Arrays.copyOfRange(payload, i + 1, i + 1 + 1)));
        Optional.of(payload.length).filter(l -> l >= i + 4).ifPresent(l -> data.setLength(Arrays.copyOfRange(payload, i + 1 + 1, i + 1 + 1 + 2)));
        Optional.of(payload.length).filter(l -> l >= i + 5).ifPresent(l -> data.setCrc(Arrays.copyOfRange(payload, i + 1 + 1 + 2, i + 1 + 1 + 2 + 1)));
        Optional.of(payload.length).filter(l -> l >= i + 5).ifPresent(l -> data.setBody(Arrays.copyOfRange(payload, i + 1 + 1 + 2 + 1, payload.length)));
        result.add(data);
        return payload.length;
    }

    // 正常判断
    private int normalDatagram(byte[] payload, List<Dg> result, int i, ChannelId id){

        byte[] head = Arrays.copyOfRange(payload, i, i + 1);
        // 1 == AFN.length
        byte[] afn = Arrays.copyOfRange(payload, i + 1, i + 1 + 1);
        // 2 == Length.length
        byte[] length = Arrays.copyOfRange(payload, i + 1 + 1, i + 1 + 1 + 2);
        boolean isBroken = i + 1 + 1 + 2 + 1 + DgTools.toShort(length) > payload.length;
        // 1 == crc.length
        byte[] crc = Arrays.copyOfRange(payload, i + 1 + 1 + 2, i + 1 + 1 + 2 + 1);
        byte[] body = Arrays.copyOfRange(payload, i + 1 + 1 + 2 + 1, Math.min(i + 1 + 1 + 2 + 1 + DgTools.toShort(length), payload.length));
        isBroken = isBroken || (!Arrays.equals(DgTools.crcCheck(body), crc));

        result.add(new Dg(id, isBroken, head, afn, length, crc, body));
        return isBroken ? payload.length : i + 1 + 1 + 2 + 1 + DgTools.toShort(length);
    }


}
