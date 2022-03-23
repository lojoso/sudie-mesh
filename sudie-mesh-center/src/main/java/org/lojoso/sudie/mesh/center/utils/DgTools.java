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
    public static byte HB_AFN = (byte) 0x00;
    public static byte[] HB_LEN = { 0x00, 0x09 };

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
