package org.lojoso.sudie.mesh.center.kernel.server.analysis.request;

import org.lojoso.sudie.mesh.center.kernel.server.analysis.Analysis;
import org.lojoso.sudie.mesh.common.model.CommonMethod;
import org.lojoso.sudie.mesh.common.model.Dg;
import org.lojoso.sudie.mesh.common.model.analysis.request.RequestBase;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RequestAnalysis implements Analysis<RequestBase> {

    public List<RequestBase> analysis(List<Dg> datas) {
        return datas.stream().map(this::decode).collect(Collectors.toList());
    }

    private RequestBase decode(Dg data) {
        RequestBase model = new RequestBase(data);
        int seq = ByteBuffer.wrap(Arrays.copyOfRange(data.getBody(), 0, 4)).getInt();
        int clength = CommonMethod.toShort(Arrays.copyOfRange(data.getBody(), 4, 4 + 2));
        model.setClassName(new String(Arrays.copyOfRange(data.getBody(), 4 + 2, 4 + 2 + clength)));
        int mlength = CommonMethod.toShort(Arrays.copyOfRange(data.getBody(), 4 + 2 + clength, 4 + 2 + clength + 2));
        model.setMethodName(new String(Arrays.copyOfRange(data.getBody(), 4 + 2 + clength + 2, 4 + 2 + clength + 2 + mlength)));
        return model;
    }

}
