package org.lojoso.sudie.mesh.provider.kernel.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.lojoso.sudie.mesh.common.config.DefaultConfig;
import org.lojoso.sudie.mesh.common.decode.decoder.ChainDecoder;
import org.lojoso.sudie.mesh.common.decode.decoder.DgDecoder;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class ProviderClient {

    private static final Bootstrap bootstrap = new Bootstrap();
    private static final NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup(DefaultConfig.CLIENT_N_THREADS);

    static {
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        // 5s 无写操作-开始心跳
                        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(DefaultConfig.LENBASED_DE_MAX_LEN,DefaultConfig.LENBASED_DE_OFFSET,DefaultConfig.LENBASED_DE_LEN,DefaultConfig.LENBASED_DE_LEN_ADJUST,0))
                                .addLast(new DgDecoder(ChainDecoder.DecoderType.REQ)).addLast(new IdleStateHandler(0, DefaultConfig.CLIENT_HB_SECONDS, 0, TimeUnit.SECONDS))
                                .addLast(new DiscardClientHandler());
                    }
                });
    }

    public static void startCluster(String config, Class<?>... classes) throws InterruptedException {
        String[] servers = Optional.ofNullable(config).map(c -> c.split(","))
                .filter(arr -> arr.length > 0)
                .orElseThrow(() -> new IllegalArgumentException(""));
        CountDownLatch count = new CountDownLatch(1);
        Stream.of(servers).forEach((server) -> ProviderClient.connect(server, count, classes));
        count.await();
    }


    public static void connect(String server, CountDownLatch count, Class<?>[] classes) {
        String[] args = server.split(":");
        bootstrap.connect(args[0], Integer.parseInt(args[1])).addListener((ChannelFutureListener) channelFuture -> {
            if (channelFuture.isSuccess()) {
                Optional.ofNullable(count).ifPresent(CountDownLatch::countDown);
                DiscardClientHandler handler = channelFuture.channel().pipeline().get(DiscardClientHandler.class);
                handler.setServer(server);
                handler.setClasses(classes);
                handler.setEncoder(Cluster.encoder);
                ClusterCache.clusters.put(channelFuture.channel().id(), server);
            } else {
                channelFuture.channel().eventLoop().schedule(() -> connect(server, count, classes), 1, TimeUnit.SECONDS);
            }
        });
    }
}
