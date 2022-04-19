package org.lojoso.sudie.mesh.common.encode.encoder;

import org.lojoso.sudie.mesh.common.config.CommonData;
import org.lojoso.sudie.mesh.common.model.CommonMethod;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ConsumerRegistryEncoder {

    public byte[] encode(String uuid) {
        byte[] bytes = uuid.getBytes(StandardCharsets.UTF_8);
        // afn + length + uuid
        ByteBuffer dataBuffer = ByteBuffer.allocate(1 + 2 + bytes.length);
        dataBuffer.put(CommonData.CD_AFN_CLI_REG);
        dataBuffer.putShort((short) bytes.length);
        dataBuffer.put(bytes);
        return dataBuffer.array();
    }
}
