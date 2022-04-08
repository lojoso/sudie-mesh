package org.lojoso.sudie.mesh.consumer.kernel.client;

import io.netty.channel.ChannelId;

import java.util.concurrent.ConcurrentHashMap;

public class ClusterCache {

    // Channel缓存
    public static final ConcurrentHashMap<ChannelId, String> clusters = new ConcurrentHashMap<>();
}
