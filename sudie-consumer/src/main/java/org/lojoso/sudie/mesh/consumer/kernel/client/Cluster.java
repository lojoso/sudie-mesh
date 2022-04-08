package org.lojoso.sudie.mesh.consumer.kernel.client;

import io.netty.channel.Channel;
import org.lojoso.sudie.mesh.common.encode.encoder.ConsumerEncoder;

import java.util.concurrent.ConcurrentHashMap;

public class Cluster {

    public static final ConsumerEncoder encoder = new ConsumerEncoder();

    public static Channel randomChannel(){
        int index = (int) (Math.random() * ClusterCache.clusterMapping.size());
        String key = ClusterCache.clusterMapping.keySet().toArray(new String[0])[index];
        return ClusterCache.clusterMapping.get(key);
    }

    public static void flush(){
        ClusterCache.clusterMapping.values().forEach(Channel::flush);
    }
}
