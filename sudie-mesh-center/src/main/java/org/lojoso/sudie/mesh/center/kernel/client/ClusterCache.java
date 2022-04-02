package org.lojoso.sudie.mesh.center.kernel.client;

import io.netty.channel.ChannelId;

import java.util.concurrent.ConcurrentHashMap;

public class ClusterCache {

    // Channel缓存
    public static final ConcurrentHashMap<ChannelId, String> clusters = new ConcurrentHashMap<>();
    // 连接重试状态
    private final ConcurrentHashMap<String, Integer> retrys = new ConcurrentHashMap<>();
}
