package org.lojoso.sudie.mesh.common.encode.encoder;

import org.lojoso.sudie.mesh.common.encode.config.FastSerialization;
import org.lojoso.sudie.mesh.common.model.CommonData;
import org.lojoso.sudie.mesh.common.model.CommonMethod;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ConsumerEncoder{

    public byte[] encode(Method method, Object[] args){

        int total = 0;
        byte[][] byteArgs = new byte[args.length][];
        for (int i = 0; i< args.length; i++) {
            byteArgs[i] = FastSerialization.getFstConfig(args[i].getClass()).get().asByteArray(args[i]);
            total += 2 + byteArgs[i].length;
        }

        byte[] className = method.getDeclaringClass().getName().getBytes(StandardCharsets.UTF_8);
        byte[] methodName = method.getName().getBytes(StandardCharsets.UTF_8);

        short dataLength = (short) (2 + className.length + 2 + methodName.length + total);
        ByteBuffer dataBuffer = ByteBuffer.allocate(dataLength);
        dataBuffer.putShort((short) className.length);
        dataBuffer.put(className);
        dataBuffer.putShort((short) methodName.length);
        dataBuffer.put(methodName);
        for (int i = 0; i< args.length; i++){
            dataBuffer.putShort((short) byteArgs[i].length);
            dataBuffer.put(byteArgs[i]);
        }

        byte[] data = dataBuffer.array();


        // head + afn + length + crc + body{len(2) + data, len(2) + data}
        ByteBuffer wholeBuffer = ByteBuffer.allocate(1 + 1 + 2 + 1 + dataLength);
        wholeBuffer.put(CommonData.HEAD);
        wholeBuffer.put(CommonData.CD_AFN);
        wholeBuffer.putShort(dataLength);
        wholeBuffer.put(CommonMethod.crcCheck(data));
        wholeBuffer.put(data);
        return wholeBuffer.array();
    }
}
