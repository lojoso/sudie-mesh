package org.lojoso.sudie.mesh.common.model;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.lojoso.sudie.mesh.common.config.CommonData.*;

public interface CommonMethod {

    byte[] HB_LEN = { 0x00, 0x08 };

//    static byte[] crcCheck(byte[] bytes){
//        byte result = (byte) 0x00;
//        for (byte b : bytes) {
//            result = (byte) (result ^ b);
//        }
//        return new byte[]{result};
//    }


    static byte[] toHeartbeat(){
        byte[] time = currentTime();
        ByteBuffer buffer = ByteBuffer.allocate(11);
        buffer.put(HB_AFN);
        buffer.put(HB_LEN);
        buffer.put(time);
        return buffer.array();
    }

    static byte[] currentTime(){
        long time = LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(8));
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(time);
        return buffer.array();
    }
}
