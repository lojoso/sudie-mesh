package org.lojoso.sudie.mesh.center.kernel.client;

import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;

public class Cluster {

    public static Channel randomChannel(){
        int index = (int) (Math.random() * ClusterCache.clusterMapping.size());
        String key = ClusterCache.clusterMapping.keySet().toArray(new String[0])[index];
        return ClusterCache.clusterMapping.get(key);
    }

    public static void flush(){
        ClusterCache.clusterMapping.values().forEach(Channel::flush);
    }

}
