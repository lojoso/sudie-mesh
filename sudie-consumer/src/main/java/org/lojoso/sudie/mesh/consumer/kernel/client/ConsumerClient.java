package org.lojoso.sudie.mesh.consumer.kernel.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class ConsumerClient {

    private static final Bootstrap bootstrap = new Bootstrap();
    private static final NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();

    static {
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        // 5s 无写操作-开始心跳
                        ch.pipeline().addLast(new IdleStateHandler(0, 30, 0, TimeUnit.MINUTES));
                    }
                });
    }


    public static void connect(String server) {
        String[] args = server.split(":");
        bootstrap.connect(args[0], Integer.parseInt(args[1])).addListener((ChannelFutureListener) channelFuture -> {
            if (channelFuture.isSuccess()) {
                Cluster.clusterMapping.put(server, channelFuture.channel());
            } else {
                channelFuture.channel().eventLoop().schedule(() -> connect(server), 1, TimeUnit.SECONDS);
            }
        });
    }
}
