package org.lojoso.sudie.mesh.provider.kernel.client;

import io.netty.channel.Channel;
import org.lojoso.sudie.mesh.common.encode.encoder.ProviderRegistryEncoder;
import org.lojoso.sudie.mesh.common.encode.encoder.ProviderResponseEncoder;

public class Cluster {

    public static final ProviderRegistryEncoder encoder = new ProviderRegistryEncoder();
    public static final ProviderResponseEncoder resEncoder = new ProviderResponseEncoder();

    public static Channel randomChannel(){
        int index = (int) (Math.random() * ClusterCache.clusterMapping.size());
        String key = ClusterCache.clusterMapping.keySet().toArray(new String[0])[index];
        return ClusterCache.clusterMapping.get(key);
    }

    public static void flush(){
        ClusterCache.clusterMapping.values().forEach(Channel::flush);
    }
}
