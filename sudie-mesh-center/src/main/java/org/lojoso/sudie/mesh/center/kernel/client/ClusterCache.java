package org.lojoso.sudie.mesh.center.kernel.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;

import java.util.concurrent.ConcurrentHashMap;

// 本地内存缓存
public class ClusterCache {

    // Channel缓存
    public static final ConcurrentHashMap<ChannelId, String> clusters = new ConcurrentHashMap<>();

    public static final ConcurrentHashMap<String, Channel> clusterMapping = new ConcurrentHashMap<>();
    // 连接重试状态
    private final ConcurrentHashMap<String, Integer> retrys = new ConcurrentHashMap<>();
}
