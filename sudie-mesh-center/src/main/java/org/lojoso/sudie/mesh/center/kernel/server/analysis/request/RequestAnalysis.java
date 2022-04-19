package org.lojoso.sudie.mesh.center.kernel.server.analysis.request;

import org.lojoso.sudie.mesh.center.kernel.server.analysis.Analysis;
import org.lojoso.sudie.mesh.common.decode.utils.DgTools;
import org.lojoso.sudie.mesh.common.model.Dg;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RequestAnalysis implements Analysis<RequestModel> {

    public List<RequestModel> analysis(List<Dg> datas) {
        return datas.stream().map(this::decode).collect(Collectors.toList());
    }

    private RequestModel decode(Dg data) {
        RequestModel model = new RequestModel(data);
        int seq = ByteBuffer.wrap(Arrays.copyOfRange(data.getBody(), 0, 4)).getInt();
        int clength = DgTools.toShort(Arrays.copyOfRange(data.getBody(), 4, 4 + 2));
        model.setClassName(new String(Arrays.copyOfRange(data.getBody(), 4 + 2, 4 + 2 + clength)));
        int mlength = DgTools.toShort(Arrays.copyOfRange(data.getBody(), 4 + 2 + clength, 4 + 2 + clength + 2));
        model.setMethodName(new String(Arrays.copyOfRange(data.getBody(), 4 + 2 + clength + 2, 4 + 2 + clength + 2 + mlength)));
        this.buildArgs(Arrays.copyOfRange(data.getBody(), 4 + 2 + clength + 2 + mlength, data.getBody().length), model);
        return model;
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

}
