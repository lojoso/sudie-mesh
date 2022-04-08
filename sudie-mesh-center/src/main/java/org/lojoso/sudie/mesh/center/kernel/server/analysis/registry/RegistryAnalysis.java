package org.lojoso.sudie.mesh.center.kernel.server.analysis.registry;

import org.lojoso.sudie.mesh.center.kernel.server.analysis.Analysis;
import org.lojoso.sudie.mesh.center.kernel.server.analysis.request.RequestModel;
import org.lojoso.sudie.mesh.common.decode.utils.DgTools;
import org.lojoso.sudie.mesh.common.model.Dg;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RegistryAnalysis implements Analysis<RegistryModel> {

    @Override
    public List<RegistryModel> analysis(List<Dg> datas) {
        return datas.stream().map(this::decode).collect(Collectors.toList());
    }

    private RegistryModel decode(Dg data){
        return this.buildClass(data.getBody(), new RegistryModel(data));
    }

    private RegistryModel buildClass(byte[] data, RegistryModel model){
        if(data.length > 0){
            int clength = DgTools.toShort(Arrays.copyOfRange(data, 0, 2));
            model.addClassName(new String(Arrays.copyOfRange(data, 2 , 2 + clength)));
            return buildClass(Arrays.copyOfRange(data, 2 + clength, data.length), model);
        }else {
            return model;
        }
    }
}
