package org.lojoso.sudie.mesh.consumer.kernel.model;

import org.lojoso.sudie.mesh.common.model.CommonState;
import org.lojoso.sudie.mesh.common.model.Dg;

public class ResponseModel extends Dg {

    private CommonState state;
    // 返回值
    private byte[] response;
    // 异常值
    private String expection;

    private String server;

    private int seq;
    private Class<?> responseType;

    public ResponseModel(){

    }

    public ResponseModel(Dg dg) {
        super.setId(dg.getId());
        // payload
        super.setAfn(dg.getAfn());
        super.setLength(dg.getLength());
        super.setBody(dg.getBody());

        super.setTotal(dg.getTotal());
    }

    public CommonState getState() {
        return state;
    }

    public void setState(CommonState state) {
        this.state = state;
    }

    public byte[] getResponse() {
        return response;
    }

    public void setResponse(byte[] response) {
        this.response = response;
    }

    public String getExpection() {
        return expection;
    }

    public void setExpection(String expection) {
        this.expection = expection;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public Class<?> getResponseType() {
        return responseType;
    }

    public void setResponseType(Class<?> responseType) {
        this.responseType = responseType;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }
}
