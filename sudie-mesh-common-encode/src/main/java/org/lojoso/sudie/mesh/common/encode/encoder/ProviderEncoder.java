package org.lojoso.sudie.mesh.common.encode.encoder;

import org.lojoso.sudie.mesh.common.model.CommonData;
import org.lojoso.sudie.mesh.common.model.CommonMethod;
import org.lojoso.sudie.mesh.common.tsvc.BaseService;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProviderEncoder{

    public byte[] encode(Class<?>... classes) {
        int total = 0;
        List<Class<?>> result = findAllService(classes);
        byte[][] byteClass = new byte[result.size()][];

        for (int i = 0; i < result.size();i++){
            byteClass[i] = result.get(i).getName().getBytes(StandardCharsets.UTF_8);
            total += 2+ byteClass[i].length;
        }

        ByteBuffer bodyBuffer = ByteBuffer.allocate(total);
        for (int i = 0; i < result.size(); i++){
            bodyBuffer.putShort((short) byteClass[i].length);
            bodyBuffer.put(byteClass[i]);
        }
        byte[] data = bodyBuffer.array();

        // head + afn + length + crc + body{len(2) + class, len(2) + class}
        ByteBuffer wholeBuffer = ByteBuffer.allocate(1 + 1 + 2 + 1 + data.length);
        wholeBuffer.put(CommonData.HEAD);
        wholeBuffer.put(CommonData.CD_AFN_REG);
        wholeBuffer.putShort((short) data.length);
        wholeBuffer.put(CommonMethod.crcCheck(data));
        wholeBuffer.put(data);

        return wholeBuffer.array();
    }

    private List<Class<?>> findAllService(Class<?>... classes){
        List<Class<?>> total = new ArrayList<>();
        Arrays.stream(classes).forEach(e -> {
            Arrays.stream(e.getInterfaces()).filter(i -> Arrays.stream(i.getInterfaces()).anyMatch(ee -> ee == BaseService.class)).forEach(total::add);
        });
        return total;
    }
}
