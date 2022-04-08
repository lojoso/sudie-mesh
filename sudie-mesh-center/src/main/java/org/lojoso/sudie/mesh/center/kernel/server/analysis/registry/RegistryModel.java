package org.lojoso.sudie.mesh.center.kernel.server.analysis.registry;

import org.lojoso.sudie.mesh.common.model.Dg;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegistryModel extends Dg {

    private List<String> classNames;

    public List<String> getClassNames() {
        return classNames;
    }

    public void setClassNames(List<String> classNames) {
        this.classNames = classNames;
    }

    public RegistryModel(){
        this.classNames = new ArrayList<>();
    }

    public RegistryModel(Dg dg) {
        super.setId(dg.getId());
        super.setBroken(dg.getBroken());
        super.setTotal(dg.getTotal());
        // payload
        super.setHead(dg.getHead());
        super.setAfn(dg.getAfn());
        super.setLength(dg.getLength());
        super.setCrc(dg.getCrc());
        super.setBody(dg.getBody());

        this.classNames = new ArrayList<>();
    }

    public RegistryModel addClassName(String className){
        this.classNames.add(className);
        return this;
    }

}
