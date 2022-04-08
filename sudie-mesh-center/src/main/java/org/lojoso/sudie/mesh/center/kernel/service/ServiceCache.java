package org.lojoso.sudie.mesh.center.kernel.service;

import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;

public class ServiceCache {

    public static final ConcurrentHashMap<String, Channel> cache = new ConcurrentHashMap<>();
}
