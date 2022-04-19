package org.lojoso.sudie.mesh.common.config;

import java.util.HashMap;

public interface NativeClassMapping {

    HashMap<Class, Class> mapping = new HashMap<Class, Class>(){{
        put(byte.class, Byte.class);
        put(boolean.class, Boolean.class);
        put(char.class, Character.class);
        put(short.class, Short.class);
        put(int.class, Integer.class);
        put(long.class, Long.class);
        put(float.class, Float.class);
        put(double.class, Double.class);
    }};
}
