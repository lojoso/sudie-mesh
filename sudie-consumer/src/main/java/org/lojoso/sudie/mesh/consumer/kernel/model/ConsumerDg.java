package org.lojoso.sudie.mesh.consumer.kernel.model;

import org.lojoso.sudie.mesh.common.model.Dg;

import java.io.Serializable;

public class ConsumerDg extends Dg implements Serializable {

    private boolean isConsumer;

    public ConsumerDg(Dg parent){
        super.setId(parent.getId());
        super.setBroken(parent.getBroken());
        super.setHead(parent.getHead());
        super.setAfn(parent.getAfn());
        super.setLength(parent.getLength());
        super.setCrc(parent.getCrc());
        super.setBody(parent.getBody());
    }

    public boolean isConsumer() {
        return isConsumer;
    }

    public void setConsumer(boolean consumer) {
        isConsumer = consumer;
    }
}
