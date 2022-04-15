package org.lojoso.sudie.mesh.common.encode.encoder;

import org.lojoso.sudie.mesh.common.encode.config.FastSerialization;
import org.lojoso.sudie.mesh.common.model.CommonData;
import org.lojoso.sudie.mesh.common.model.CommonMethod;
import org.lojoso.sudie.mesh.common.model.CommonState;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;

/**
 * 返回值报文encode
 */
public class ResponseEncoder {

    public <T> byte[] encode(CommonState state, short channelId,T result, String exception){

        return Optional.of(state).filter(s -> Objects.equals(state, CommonState.SUCCESS_NO_RES)).map(s -> {
            ByteBuffer buffer = ByteBuffer.allocate(1 + 1 + 2 + 1 + 1);
            buffer.put(CommonData.HEAD);
            buffer.put(CommonData.CD_AFN_RES);
            buffer.putShort((short) 1);
            buffer.put((byte) state.getCode());
            buffer.putShort(channelId);
            buffer.put((byte) state.getCode());
            return buffer.array();
        }).orElseGet(() -> {
            // 带有结构体 （head + afn + length + crc + channelId + state + body_length + body）
            byte[] res = Optional.ofNullable(exception).map(ex -> ex.getBytes(StandardCharsets.UTF_8))
                    .orElseGet(() -> FastSerialization.getFstConfig(result.getClass()).get().asByteArray(result));
            int total = 1 + 2 + res.length;
            ByteBuffer buffer = ByteBuffer.allocate(1 + 1 + 2 + 1 + total);
            ByteBuffer bodyBuffer = ByteBuffer.allocate(2 + 2 + res.length);
            bodyBuffer.putShort((byte) state.getCode());
            bodyBuffer.putShort((short) res.length);
            bodyBuffer.put(res);
            byte[] body = bodyBuffer.array();

            buffer.put(CommonData.HEAD);
            buffer.put(CommonData.CD_AFN_RES);
            buffer.putShort((short) total);
            buffer.put(CommonMethod.crcCheck(body));
            buffer.putShort(channelId);
            buffer.put(body);
            return buffer.array();
        });
    }
}
