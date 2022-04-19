package org.lojoso.sudie.mesh.common.model.analysis.request;

import org.lojoso.sudie.mesh.common.model.CommonMethod;
import org.lojoso.sudie.mesh.common.model.Dg;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RequestBase extends Dg {

    private String className;
    private String methodName;
    private int channelIndex;

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

    public int getChannelIndex() {
        return channelIndex;
    }

    public void setChannelIndex(int channelIndex) {
        this.channelIndex = channelIndex;
        super.setTotal(super.getTotal() + 2);
    }

    public RequestBase() {
    }

    public RequestBase(Dg dg) {
        super.setId(dg.getId());
        // payload
        super.setAfn(dg.getAfn());
        super.setLength(dg.getLength());
        super.setBody(dg.getBody());

        super.setTotal(dg.getTotal());
    }

    public byte[] rebuild() {
        ByteBuffer buffer = ByteBuffer.allocate(super.getTotal());
        Optional.ofNullable(super.getAfn()).ifPresent(buffer::put);
        Optional.ofNullable(super.getLength()).ifPresent(bytes -> {
            ByteBuffer nLength = ByteBuffer.allocate(2);
            nLength.putShort((short) (CommonMethod.toShort(super.getLength()) + 2));
            buffer.put(nLength.array());
        });
        Optional.ofNullable(super.getBody()).ifPresent(bytes -> {

            ByteBuffer newBody = ByteBuffer.allocate(2 + bytes.length);
            newBody.putShort((short) channelIndex);
            newBody.put(bytes);
            // new Body
            buffer.put(newBody.array());
        });
        return buffer.array();
    }


}
