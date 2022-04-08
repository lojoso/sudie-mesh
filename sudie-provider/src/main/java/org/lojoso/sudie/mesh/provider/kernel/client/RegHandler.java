package org.lojoso.sudie.mesh.provider.kernel.client;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

public class RegHandler {

    public static void regToCluster(byte[] payload){
        ClusterCache.clusterMapping.values().forEach(e -> e.writeAndFlush(Unpooled.wrappedBuffer(payload)));
    }

    public static void regToCluster(Channel channel, byte[] payload){
        channel.writeAndFlush(Unpooled.wrappedBuffer(payload));
    }
}
