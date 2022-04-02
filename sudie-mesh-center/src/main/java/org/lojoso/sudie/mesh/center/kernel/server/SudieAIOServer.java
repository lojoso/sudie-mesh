package org.lojoso.sudie.mesh.center.kernel.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.lojoso.sudie.mesh.center.config.SudieBaseConfig;
import org.lojoso.sudie.mesh.center.kernel.client.SudieAIOClient;
import org.lojoso.sudie.mesh.center.kernel.data.BrokenDgPool;
import org.lojoso.sudie.mesh.center.kernel.decoder.DgDecoder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SudieAIOServer {

    public SudieAIOServer() {
        this(new SudieBaseConfig() {{
            setPort(60001);
            setClusters("localhost:60002");
        }});
    }

    private void startCluster(SudieBaseConfig config) {
        Thread cluster = new Thread(() -> new SudieAIOClient(config));
        cluster.start();
    }

    public SudieAIOServer(SudieBaseConfig config) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup(5);
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            // 60s 无读取-关闭连接
                            ch.pipeline().addLast(new IdleStateHandler(60, 0,0, TimeUnit.MINUTES))
                                    .addLast(new DgDecoder()).addLast(new DiscardServerHandler());
                        }
                    })
                    .childOption(NioChannelOption.SO_KEEPALIVE, true);
            ChannelFuture f = b.bind(config.getPort()).sync();
            f.addListener(future -> this.startCluster(config));
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void start(SudieBaseConfig config) {
        new SudieAIOServer(config);
    }
}
