package org.lojoso.sudie.mesh.common.model.analysis.registry;

import org.lojoso.sudie.mesh.common.model.Dg;

import java.util.ArrayList;
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
        super.setTotal(dg.getTotal());
        // payload
        super.setAfn(dg.getAfn());
        super.setLength(dg.getLength());
        super.setBody(dg.getBody());

        this.classNames = new ArrayList<>();
    }

    public RegistryModel addClassName(String className){
        this.classNames.add(className);
        return this;
    }

}
