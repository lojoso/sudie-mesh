package org.lojoso.sudie.mesh.common.encode.encoder;

import org.lojoso.sudie.mesh.common.encode.config.FastSerialization;
import org.lojoso.sudie.mesh.common.config.CommonData;
import org.lojoso.sudie.mesh.common.model.CommonMethod;
import org.lojoso.sudie.mesh.common.model.CommonState;
import org.lojoso.sudie.mesh.common.model.provider.RequestModel;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ProviderResponseEncoder {

    public <T> byte[] encode(CommonState state, short channelId, T result, int seq, String exception){

        return Optional.of(state).filter(s -> Objects.equals(state, CommonState.SUCCESS_NO_RES)).map(s -> {
            ByteBuffer buffer = ByteBuffer.allocate(1 + 2 + 2 + 1);
            ByteBuffer body = ByteBuffer.allocate(7);
            body.putShort(channelId);
            body.putInt(seq);
            body.put((byte) state.getCode());

            buffer.put(CommonData.CD_AFN_RES);
            buffer.putShort((short) 7);
            buffer.put(body.array());
            return buffer.array();
        }).orElseGet(() -> {
            // 带有结构体 （head + afn + length + crc + channelId + state + body_length + body）
            byte[] res = Optional.ofNullable(exception).map(ex -> ex.getBytes(StandardCharsets.UTF_8))
                    .orElseGet(() -> FastSerialization.getFstConfig(result.getClass()).get().asByteArray(result));
            int bodyTotal = 4 + 2 + 1 + 2 + res.length;
            ByteBuffer buffer = ByteBuffer.allocate(1 + 2 + bodyTotal);
            ByteBuffer bodyBuffer = ByteBuffer.allocate(bodyTotal);
            bodyBuffer.putShort(channelId);
            bodyBuffer.putInt(seq);
            bodyBuffer.put((byte) state.getCode());
            bodyBuffer.putShort((short) res.length);
            bodyBuffer.put(res);
            byte[] body = bodyBuffer.array();

            buffer.put(CommonData.CD_AFN_RES);
            buffer.putShort((short) bodyTotal);
            buffer.put(body);
            return buffer.array();
        });
    }
}
