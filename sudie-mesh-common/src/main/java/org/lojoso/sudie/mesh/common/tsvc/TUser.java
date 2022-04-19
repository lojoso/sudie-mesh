package org.lojoso.sudie.mesh.common.tsvc;

import java.io.Serializable;

public class TUser implements Serializable {

    private String userName;
    private int seq;

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

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    @Override
    public String toString() {
        return "TUser{" +
                "userName='" + userName + '\'' +
                ", seq=" + seq +
                '}';
    }
}
