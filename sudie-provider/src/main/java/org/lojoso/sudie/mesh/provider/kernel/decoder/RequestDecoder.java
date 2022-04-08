package org.lojoso.sudie.mesh.provider.kernel.decoder;

import org.lojoso.sudie.mesh.common.decode.decoder.ChainDecoder;
import org.lojoso.sudie.mesh.common.decode.utils.DgTools;
import org.lojoso.sudie.mesh.common.encode.config.FastSerialization;
import org.lojoso.sudie.mesh.common.model.Dg;
import org.lojoso.sudie.mesh.provider.kernel.client.ClusterCache;
import org.lojoso.sudie.mesh.provider.kernel.data.RequestModel;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.lojoso.sudie.mesh.common.model.CommonData.SD_AFN_PUSH;

public class RequestDecoder implements ChainDecoder {


    @Override
    public List<? extends Dg> chainDecoder(List<Dg> data) {
        return data.stream().filter(d -> Arrays.equals(d.getAfn(), SD_AFN_PUSH)).map(this::decode).collect(Collectors.toList());
    }

    private RequestModel decode(Dg data) {
        RequestModel model = new RequestModel(data);
        int clength = DgTools.toShort(Arrays.copyOfRange(data.getBody(), 0, 2));
        model.setClassName(new String(Arrays.copyOfRange(data.getBody(), 2, 2 + clength)));
        int mlength = DgTools.toShort(Arrays.copyOfRange(data.getBody(), 2 + clength, 2 + clength + 2));
        model.setMethodName(new String(Arrays.copyOfRange(data.getBody(), 2 + clength + 2, 2 + clength + 2 + mlength)));
        this.buildArgs(Arrays.copyOfRange(data.getBody(), 2 + clength + 2 + mlength, data.getBody().length), model);
        return this.hitMatch(model);
    }

    private RequestModel buildArgs(byte[] data, RequestModel model) {
        if (data.length > 0) {
            int length = DgTools.toShort(Arrays.copyOfRange(data, 0, 2));
            model.addArgs(Arrays.copyOfRange(data, 2, 2 + length));
            return buildArgs(Arrays.copyOfRange(data, 2 + length, data.length), model);
        } else {
            return model;
        }
    }

    private RequestModel hitMatch(RequestModel model) {
        try {
            Arrays.stream(Class.forName(model.getClassName()).getMethods())
                    .filter(m -> Objects.equals(m.getName(), model.getMethodName()))
                    .filter(m -> Objects.equals(m.getParameterCount(), model.getArgsCount()))
                    .filter(m -> IntStream.range(0, m.getParameterCount()).boxed().allMatch(t -> this.tryFst(model.getArgsData().get(t), m.getParameterTypes()[t])))
                    .findFirst().ifPresent(m -> {
                        model.setTargetMethod(m);
                        model.setTarget(ClusterCache.serviceMapping.get(model.getClassName()));
                        model.setTargetParams(this.confirmFst(model.getArgsData(), m.getParameterTypes()));
                    });
            return model;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return model;
        }
    }

    private boolean tryFst(byte[] data, Class<?> type) {
        try {
            FastSerialization.getFstConfig(type).get().asObject(Arrays.copyOfRange(data, 0, data.length));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private Object[] confirmFst(List<byte[]> datas, Class<?>[] types) {

        Object[] params = new Object[types.length];
        for (int i = 0; i < types.length; i++) {
            params[i] = FastSerialization.getFstConfig(types[i]).get().asObject(Arrays.copyOfRange(datas.get(i), 0, datas.get(i).length));
        }
        return params;
    }

}