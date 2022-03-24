package org.lojoso.sudie.mesh.center.kernel.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.lojoso.sudie.mesh.center.config.SudieBaseConfig;
import org.lojoso.sudie.mesh.center.kernel.decoder.DgDecoder;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class SudieAIOClient {

    // 集群构建
    public SudieBaseConfig config;
    // Channel缓存
    public final ConcurrentHashMap<ChannelId, String> clusters = new ConcurrentHashMap<>();
    // 连接重试状态
    private final ConcurrentHashMap<String, Integer> retrys = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Runnable> cacheScripts = new ConcurrentHashMap<>();
    private final Thread.UncaughtExceptionHandler defaultHandler = (t, e) -> {
        t.interrupt();
        retrys.compute(t.getName(), (k, v) -> Optional.ofNullable(v).map(i -> ++i).orElse(1));
        if(retrys.get(t.getName()) > config.getRetry()){
//            System.out.printf("cannot connect to server :%s failed\n", t.getName());
        }else {
//            System.out.printf("cannot connect to server :%s retry: %s/%s \n", t.getName(), retrys.get(t.getName()), config.getRetry());
            this.startConnect(t.getName(), cacheScripts.get(t.getName()));
        }
    };


    public SudieAIOClient() {}

    public SudieAIOClient(SudieBaseConfig config) {
        this.config = config;
        this.startCluster(config.getClusters());
    }

    private void startCluster(String config) {
        String[] servers = Optional.ofNullable(config).map(c -> c.split(",")).filter(arr -> arr.length > 0)
                .orElseThrow(() -> new IllegalArgumentException(""));
        for (String server : servers) {
            String[] info = server.split(":");
            cacheScripts.putIfAbsent(server, () -> this.start(server, info[0], Integer.parseInt(info[1])));
            this.startConnect(server, cacheScripts.get(server));
        }
    }

    private void startConnect(String server, Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setName(server);
        thread.setUncaughtExceptionHandler(defaultHandler);
        thread.start();
    }

    private void start(String server, String host, int port) {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap().group(workerGroup).channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            clusters.putIfAbsent(ch.id(), server);
                            ch.pipeline().addLast(new DgDecoder()).addLast(new IdleStateHandler(1, 0, 0, TimeUnit.MINUTES))
                                    .addLast(new DiscardClientHandler(server, clusters));
                        }
                    });
            ChannelFuture future = bootstrap.connect(host, port).sync();
            future.addListener(future1 -> System.out.printf("%s is connected\n", server));
            future.channel().closeFuture().sync();
        } catch (InterruptedException excepton) {
            excepton.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

}
