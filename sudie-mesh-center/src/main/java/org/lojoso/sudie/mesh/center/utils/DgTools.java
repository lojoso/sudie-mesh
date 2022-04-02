package org.lojoso.sudie.mesh.center.utils;

import io.netty.channel.ChannelId;
import org.lojoso.sudie.mesh.center.kernel.model.Dg;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.stream.IntStream;

public class DgTools {

    public static byte[] HEAD = { 0x68 };
    // 心跳
    public static byte[] HB_AFN = { 0x00 };
    public static byte[] HB_LEN = { 0x00, 0x09 };

    // 客户端
    public static byte[] CD_AFN = { 0x01 };
    // 服务端 均衡拉取
    public static byte[] SD_AFN_PULL = { 0x11 };
    // 服务端 均衡入队列
    public static byte[] SD_AFN_PUSH = { 0x12 };


    // 无符号
    public static int toShort(byte[] bytes){
        return (ByteBuffer.wrap(bytes).getShort() & 0xffff);
    }

    public static byte[] buildBytes(byte[] head, byte[] tail){
        ByteBuffer buffer = ByteBuffer.allocate(head.length + tail.length);
        buffer.put(head);
        buffer.put(tail);
        return buffer.array();
    }

    public static Dg checkVaild(byte[] payload, ChannelId id){
        if(payload.length >= 1 + 1 + 2 + 1){
            byte[] head = Arrays.copyOfRange(payload, 0, 1);
            // 1 == AFN.length
            byte[] afn = Arrays.copyOfRange(payload, 1, 1 + 1);
            // 2 == Length.length
            byte[] length = Arrays.copyOfRange(payload, 1 + 1, 1 + 1 + 2);
            boolean isBroken = 1 + 1 + 2 + 1 + DgTools.toShort(length) > payload.length;
            // 1 == crc.length
            byte[] crc = Arrays.copyOfRange(payload, 1 + 1 + 2, 1 + 1 + 2 + 1);
            byte[] body = Arrays.copyOfRange(payload, 1 + 1 + 2 + 1, Math.min(1 + 1 + 2 + 1 + DgTools.toShort(length), payload.length));
            isBroken = isBroken || (!Arrays.equals(DgTools.crcCheck(body), crc));
            return new Dg(id, isBroken, head, afn, length, crc, body);
        }
        return new Dg(id, payload);
    }

    public static byte[] crcCheck(byte[] bytes){
        byte result = (byte) 0x00;
        for (byte b : bytes) {
            result = (byte) (result ^ b);
        }
        return new byte[]{result};
    }


    public static byte[] toHeartbeat(){
        byte[] time = currentTime();
        ByteBuffer buffer = ByteBuffer.allocate(13);
        buffer.put(HEAD);
        buffer.put(HB_AFN);
        buffer.put(HB_LEN);
        buffer.put(crcCheck(time));
        buffer.put(time);
        return buffer.array();
    }

    public static byte[] currentTime(){
        long time = LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(8));
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(time);
        return buffer.array();
    }
}
