package org.lojoso.sudie.mesh.center.kernel.server.analysis.response;

import org.lojoso.sudie.mesh.center.kernel.server.analysis.Analysis;
import org.lojoso.sudie.mesh.center.kernel.server.analysis.request.RequestModel;
import org.lojoso.sudie.mesh.common.decode.utils.DgTools;
import org.lojoso.sudie.mesh.common.model.CommonState;
import org.lojoso.sudie.mesh.common.model.Dg;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ResponseAnalysis implements Analysis<ResponseModel> {


    @Override
    public List<ResponseModel> analysis(List<Dg> datas) {
        return null;
    }

    private ResponseModel decode(Dg data){
        // state + length + body
        ResponseModel model = new ResponseModel(data);
        CommonState state = CommonState.getByCode(DgTools.toShort(Arrays.copyOfRange(data.getBody(), 0, 1)));
        model.setState(state);
        if(Objects.equals(state, CommonState.SUCCESS_NO_RES)){
            return model;
        }else {
            int length = DgTools.toShort(Arrays.copyOfRange(data.getBody(), 1, 1 + 2));
            if(Objects.equals(state, CommonState.EXCEPTION)){
                model.setExpection(new String(Arrays.copyOfRange(data.getBody(), 1 + 2, 1 + 2 + length), StandardCharsets.UTF_8));
                return model;
            }else {
//                model.setResponse();
                return model;
            }
        }
    }
}
