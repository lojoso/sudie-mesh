package org.lojoso.sudie.mesh.consumer.kernel.decoder;

import org.lojoso.sudie.mesh.common.decode.decoder.ChainDecoder;
import org.lojoso.sudie.mesh.common.decode.utils.DgTools;
import org.lojoso.sudie.mesh.common.model.CommonState;
import org.lojoso.sudie.mesh.common.model.Dg;
import org.lojoso.sudie.mesh.consumer.kernel.model.ResponseModel;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.lojoso.sudie.mesh.common.config.CommonData.CD_AFN_RES;

public class ResponseDecoder implements ChainDecoder {


    @Override
    public List<? extends Dg> chainDecoder(List<Dg> data) {
        return data.stream().filter(d -> Arrays.equals(d.getAfn(), CD_AFN_RES)).map(this::decode).collect(Collectors.toList());
    }


    private ResponseModel decode(Dg dg) {
        ResponseModel model = new ResponseModel(dg);

        // client + seq + state + length + body
        int channelIndex = DgTools.toShort(Arrays.copyOfRange(model.getBody(), 0, 2));
        int seq = ByteBuffer.wrap(Arrays.copyOfRange(model.getBody(), 2, 2 + 4)).getInt();
        model.setSeq(seq);
        CommonState state = CommonState.getByCode(DgTools.toByte(Arrays.copyOfRange(model.getBody(), 2 + 4, 2 + 4 + 1)));
        model.setState(state);
        if (!Objects.equals(state, CommonState.SUCCESS_NO_RES)) {
            int length = DgTools.toShort(Arrays.copyOfRange(model.getBody(), 2 + 4 + 1, 2 + 4 + 1 + 2));
            if (Objects.equals(state, CommonState.EXCEPTION)) {
                model.setExpection(new String(Arrays.copyOfRange(model.getBody(), 2 + 4 + 1 + 2, 2 + 4 + 1 + 2 + length), StandardCharsets.UTF_8));
            } else {
                model.setResponse(Arrays.copyOfRange(model.getBody(), 2 + 4 + 1 + 2, 2 + 4 + 1 + 2 + length));
            }
        }
        return model;
    }

}
