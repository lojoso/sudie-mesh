package org.lojoso.sudie.mesh.center.kernel.server.analysis.request;

import org.lojoso.sudie.mesh.common.decode.utils.DgTools;
import org.lojoso.sudie.mesh.common.model.CommonMethod;
import org.lojoso.sudie.mesh.common.model.Dg;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class RequestModel extends Dg {

    private String className;
    private String methodName;
    private int argsCount;
    private List<byte[]> argsData;
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

    public List<byte[]> getArgsData() {
        return argsData;
    }

    public void setArgsData(List<byte[]> argsData) {
        this.argsData = argsData;
    }

    public int getChannelIndex() {
        return channelIndex;
    }

    public void setChannelIndex(int channelIndex) {
        this.channelIndex = channelIndex;
        super.setTotal(super.getTotal() + 2);
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

    public byte[] rebuild() {
        ByteBuffer buffer = ByteBuffer.allocate(super.getTotal());
        Optional.ofNullable(super.getAfn()).ifPresent(buffer::put);
        Optional.ofNullable(super.getLength()).ifPresent(bytes -> {
            ByteBuffer nLength = ByteBuffer.allocate(2);
            nLength.putShort((short) (DgTools.toShort(super.getLength()) + 2));
            buffer.put(nLength.array());
        });
        Optional.ofNullable(super.getBody()).ifPresent(bytes -> {

            ByteBuffer newBody = ByteBuffer.allocate(2 + bytes.length);
            newBody.putShort((short) channelIndex);
            newBody.put(bytes);
            // new CRC
            // new Body
            buffer.put(newBody.array());
        });
        return buffer.array();
    }

    public RequestModel addArgs(byte[] obj){
        this.argsCount += 1;
        this.argsData.add(obj);
        return this;
    }

}
