package org.lojoso.sudie.mesh.consumer.kernel.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;

import java.util.concurrent.ConcurrentHashMap;

public class ClusterCache {

    // Channel缓存
    public static final ConcurrentHashMap<ChannelId, String> clusters = new ConcurrentHashMap<>();

    public static final ConcurrentHashMap<String, Channel> clusterMapping = new ConcurrentHashMap<>();

}
