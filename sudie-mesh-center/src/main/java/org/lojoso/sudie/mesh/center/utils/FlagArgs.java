package org.lojoso.sudie.mesh.center.utils;

import java.util.Objects;

public class FlagArgs {

    public static String getValue(String[] array, String flag, String defValue){
        for (int i = 0; i< array.length; i++){
            if(Objects.equals(array[i].trim(), flag.trim())){
                return i > array.length - 2 ? defValue : array[i+1];
            }
        }
        return defValue;
    }
}
