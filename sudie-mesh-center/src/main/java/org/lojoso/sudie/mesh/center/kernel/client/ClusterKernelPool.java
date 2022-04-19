package org.lojoso.sudie.mesh.center.kernel.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.AbstractEventExecutorGroup;
import org.lojoso.sudie.mesh.common.config.DefaultConfig;
import org.lojoso.sudie.mesh.common.decode.decoder.DgDecoder;

import java.io.Closeable;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class ClusterKernelPool implements Closeable {

    public static NioEventLoopGroup group;
    public static Bootstrap bootstrap = new Bootstrap();

    static {
        group = new NioEventLoopGroup();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        // 5s 无写操作-开始心跳
                        ch.pipeline()
                                .addLast(new LengthFieldBasedFrameDecoder(DefaultConfig.LENBASED_DE_MAX_LEN, DefaultConfig.LENBASED_DE_OFFSET, DefaultConfig.LENBASED_DE_LEN,DefaultConfig.LENBASED_DE_LEN_ADJUST,0))
                                .addLast(new DgDecoder()).addLast(new IdleStateHandler(0,DefaultConfig.CLIENT_HB_SECONDS, 0, TimeUnit.SECONDS))
                                .addLast(new DiscardClientHandler(ClusterCache.clusters));
                    }
                });
    }

    public static void connect(String server) {
        String[] args = server.split(":");
        bootstrap.connect(args[0], Integer.parseInt(args[1])).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                future.channel().pipeline().get(DiscardClientHandler.class).setServer(server);
                ClusterCache.clusters.put(future.channel().id(), server);
            } else {
                future.channel().eventLoop().schedule(() -> connect(server), 1, TimeUnit.SECONDS);
            }
        });
    }

    @Override
    public void close() {
        Optional.ofNullable(group).ifPresent(AbstractEventExecutorGroup::shutdownGracefully);
    }
}
