package org.lojoso.sudie.mesh.provider.kernel.client;

import io.netty.buffer.Unpooled;

public class RegHandler {

    public static void regToCluster(byte[] payload){
        Cluster.clusterMapping.values().forEach(e -> e.writeAndFlush(Unpooled.wrappedBuffer(payload)));
    }
}
