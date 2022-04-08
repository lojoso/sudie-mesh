package org.lojoso.sudie.mesh.common.encode.config;

import org.nustaq.serialization.FSTConfiguration;

import java.util.HashMap;
import java.util.Map;

public class FastSerialization {

    public static Map<Class<?>, ThreadLocal<FSTConfiguration>> localCache = new HashMap<>();

    public static ThreadLocal<FSTConfiguration> getFstConfig(Class<?> clazz){
        return localCache.computeIfAbsent(clazz, c -> localCache.getOrDefault(clazz, ThreadLocal.withInitial(() -> {
            FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();
            conf.registerClass(clazz);
            conf.setShareReferences(false);
            return conf;
        })));
    }
}
