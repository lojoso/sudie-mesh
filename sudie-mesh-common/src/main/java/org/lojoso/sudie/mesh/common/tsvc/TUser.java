package org.lojoso.sudie.mesh.common.tsvc;

import java.io.Serializable;

public class TUser implements Serializable {

    private String userName;

    public TUser(){

    }

    public TUser(String user){
        this.userName = user;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
