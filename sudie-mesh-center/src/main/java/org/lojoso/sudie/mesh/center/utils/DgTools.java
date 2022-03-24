package org.lojoso.sudie.mesh.center.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
