package org.lojoso.sudie.mesh.center.kernel.server.analysis.response;

import org.lojoso.sudie.mesh.common.model.CommonState;
import org.lojoso.sudie.mesh.common.model.Dg;

public class ResponseModel extends Dg {

    private CommonState state;
    // 返回值
    private Object response;
    // 异常值
    private String expection;

    public ResponseModel(){

    }

    public ResponseModel(Dg dg) {
        super.setId(dg.getId());
        super.setBroken(dg.getBroken());
        super.setTotal(dg.getTotal());
        // payload
        super.setHead(dg.getHead());
        super.setAfn(dg.getAfn());
        super.setLength(dg.getLength());
        super.setCrc(dg.getCrc());
        super.setBody(dg.getBody());
    }

    public CommonState getState() {
        return state;
    }

    public void setState(CommonState state) {
        this.state = state;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public String getExpection() {
        return expection;
    }

    public void setExpection(String expection) {
        this.expection = expection;
    }
}
