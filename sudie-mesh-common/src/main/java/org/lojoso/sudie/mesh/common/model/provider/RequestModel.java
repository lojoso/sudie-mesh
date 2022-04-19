package org.lojoso.sudie.mesh.common.model.provider;

import org.lojoso.sudie.mesh.common.model.Dg;
import org.lojoso.sudie.mesh.common.model.analysis.request.RequestBase;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class RequestModel extends RequestBase {

    private int argsCount;
    private List<byte[]> argsData;

    private int channelIndex;

    private Object target;
    private Method targetMethod;
    private Object[] targetParams;
    private int seq;


    public List<byte[]> getArgsData() {
        return argsData;
    }

    public void setArgsData(List<byte[]> argsData) {
        this.argsData = argsData;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    public void setTargetMethod(Method targetMethod) {
        this.targetMethod = targetMethod;
    }

    public Object[] getTargetParams() {
        return targetParams;
    }

    public void setTargetParams(Object[] targetParams) {
        this.targetParams = targetParams;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public RequestModel() {
        this.argsCount = 0;
        this.argsData = new ArrayList<>();
    }

    public RequestModel(Dg dg) {
        super.setId(dg.getId());
        // payload
        super.setAfn(dg.getAfn());
        super.setLength(dg.getLength());
        super.setBody(dg.getBody());
        super.setTotal(dg.getTotal());

        this.argsCount = 0;
        this.argsData = new ArrayList<>();
    }

    public void addArgs(byte[] obj){
        this.argsCount += 1;
        this.argsData.add(obj);
    }

    public int getArgsCount() {
        return argsCount;
    }

    public int getChannelIndex() {
        return channelIndex;
    }

    public void setChannelIndex(int channelIndex) {
        this.channelIndex = channelIndex;
    }
}
