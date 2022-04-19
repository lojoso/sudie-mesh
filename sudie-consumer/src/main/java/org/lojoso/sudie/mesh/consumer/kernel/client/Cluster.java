package org.lojoso.sudie.mesh.consumer.kernel.client;

import io.netty.channel.Channel;
import org.lojoso.sudie.mesh.common.encode.encoder.ConsumerRegistryEncoder;
import org.lojoso.sudie.mesh.common.encode.encoder.ConsumerRequestEncoder;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class Cluster {

    // uuid标识
    public static AtomicInteger seq = new AtomicInteger(0);

    public static final String uuid = UUID.randomUUID().toString();
    public static final ConsumerRequestEncoder reqEncoder = new ConsumerRequestEncoder();
    public static final ConsumerRegistryEncoder regEncoder = new ConsumerRegistryEncoder();

    public static Channel randomChannel(){
        int index = (int) (Math.random() * ClusterCache.clusterMapping.size());
        String key = ClusterCache.clusterMapping.keySet().toArray(new String[0])[index];
        return ClusterCache.clusterMapping.get(key);
    }

    public static void flush(){
        ClusterCache.clusterMapping.values().forEach(Channel::flush);
    }
}
