package org.lojoso.sudie.mesh.consumer.kernel.client;

import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;

public class Cluster {

    public static final ConcurrentHashMap<String, Channel> clusterMapping = new ConcurrentHashMap<>();

    public static Channel randomChannel(){
        int index = (int) (Math.random() * clusterMapping.size());
        String key = clusterMapping.keySet().toArray(new String[0])[index];
        return clusterMapping.get(key);
    }

    public static void flush(){
        clusterMapping.values().forEach(Channel::flush);
    }
}
