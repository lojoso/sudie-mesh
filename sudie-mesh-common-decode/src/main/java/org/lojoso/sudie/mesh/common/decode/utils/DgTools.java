package org.lojoso.sudie.mesh.common.decode.utils;

import io.netty.channel.ChannelId;
import org.lojoso.sudie.mesh.common.model.CommonMethod;
import org.lojoso.sudie.mesh.common.model.Dg;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;

import static org.lojoso.sudie.mesh.common.model.CommonData.*;

public class DgTools {


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
            isBroken = isBroken || (!Arrays.equals(CommonMethod.crcCheck(body), crc));
            return new Dg(id, isBroken, head, afn, length, crc, body);
        }
        return new Dg(id, payload);
    }

}
