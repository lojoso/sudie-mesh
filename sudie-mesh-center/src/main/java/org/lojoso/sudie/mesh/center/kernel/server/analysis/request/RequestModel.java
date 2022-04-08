package org.lojoso.sudie.mesh.center.kernel.server.analysis.request;

import org.lojoso.sudie.mesh.common.model.Dg;

import java.util.ArrayList;
import java.util.List;

public class RequestModel extends Dg {

    private String className;
    private String methodName;
    private int argsCount;
    private List<byte[]> argsData;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<byte[]> getArgsData() {
        return argsData;
    }

    public void setArgsData(List<byte[]> argsData) {
        this.argsData = argsData;
    }

    public RequestModel() {
        this.argsCount = 0;
        this.argsData = new ArrayList<>();
    }

    public RequestModel(Dg dg) {
        super.setId(dg.getId());
        super.setBroken(dg.getBroken());
        super.setTotal(dg.getTotal());
        // payload
        super.setHead(dg.getHead());
        super.setAfn(dg.getAfn());
        super.setLength(dg.getLength());
        super.setCrc(dg.getCrc());
        super.setBody(dg.getBody());

        this.argsCount = 0;
        this.argsData = new ArrayList<>();
    }

    public RequestModel addArgs(byte[] obj){
        this.argsCount += 1;
        this.argsData.add(obj);
        return this;
    }

}
