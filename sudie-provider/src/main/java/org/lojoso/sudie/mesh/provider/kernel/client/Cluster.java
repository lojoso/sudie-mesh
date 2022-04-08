package org.lojoso.sudie.mesh.provider.kernel.client;

import io.netty.channel.Channel;
import org.lojoso.sudie.mesh.common.encode.encoder.ConsumerEncoder;

import java.util.concurrent.ConcurrentHashMap;

public class Cluster {

    public static final ConsumerEncoder encoder = new ConsumerEncoder();
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
