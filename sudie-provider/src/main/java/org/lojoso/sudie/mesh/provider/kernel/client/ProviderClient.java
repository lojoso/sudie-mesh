package org.lojoso.sudie.mesh.provider.kernel.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.lojoso.sudie.mesh.common.decode.decoder.DgDecoder;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class ProviderClient {

    private static final Bootstrap bootstrap = new Bootstrap();
    private static final NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();

    static {
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        // 5s 无写操作-开始心跳
                        ch.pipeline().addLast(new DgDecoder()).addLast(new IdleStateHandler(0, 30, 0, TimeUnit.MINUTES))
                                .addLast(new DiscardClientHandler());
                    }
                });
    }

    public static void startCluster(String config) throws InterruptedException {
        String[] servers = Optional.ofNullable(config).map(c -> c.split(","))
                .filter(arr -> arr.length > 0)
                .orElseThrow(() -> new IllegalArgumentException(""));
        CountDownLatch count = new CountDownLatch(1);
        Stream.of(servers).forEach((server) -> ProviderClient.connect(server, count));
        count.await();
    }


    public static void connect(String server, CountDownLatch count) {
        String[] args = server.split(":");
        bootstrap.connect(args[0], Integer.parseInt(args[1])).addListener((ChannelFutureListener) channelFuture -> {
            if (channelFuture.isSuccess()) {
                Optional.ofNullable(count).ifPresent(CountDownLatch::countDown);
                channelFuture.channel().pipeline().get(DiscardClientHandler.class).setServer(server);
                ClusterCache.clusters.put(channelFuture.channel().id(), server);
            } else {
                channelFuture.channel().eventLoop().schedule(() -> connect(server, count), 1, TimeUnit.SECONDS);
            }
        });
    }
}
