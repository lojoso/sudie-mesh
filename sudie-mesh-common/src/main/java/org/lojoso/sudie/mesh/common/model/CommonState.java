package org.lojoso.sudie.mesh.common.model;

import java.util.Arrays;
import java.util.Objects;

public enum CommonState {

    WAITING(0),
    SUCCESS_NO_RES(1),
    SUCCESS_RES(2),
    EXCEPTION(3),
    TIMEOUT(4);

    private int code;

    CommonState(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static CommonState getByCode(int code){
        return Arrays.stream(CommonState.values()).filter(e -> Objects.equals(e.getCode(), code)).findFirst().orElse(null);
    }

}
