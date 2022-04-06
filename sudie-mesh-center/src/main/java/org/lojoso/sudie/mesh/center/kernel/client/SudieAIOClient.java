package org.lojoso.sudie.mesh.center.kernel.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.lojoso.sudie.mesh.center.config.SudieBaseConfig;
import sun.misc.Unsafe;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import java.util.stream.Stream;

public class SudieAIOClient {

    // 集群构建
    public SudieBaseConfig config;

    public SudieAIOClient() {
    }

    public SudieAIOClient(SudieBaseConfig config) {
        this.config = config;
        this.startCluster(config.getClusters());
    }

    private void startCluster(String config) {
        String[] servers = Optional.ofNullable(config).map(c -> c.split(","))
                .filter(arr -> arr.length > 0)
                .orElseThrow(() -> new IllegalArgumentException(""));
        Stream.of(servers).forEach(ClusterKernelPool::connect);
        LockSupport.park();
    }
}
