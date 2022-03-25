package org.lojoso.sudie.mesh.center.kernel.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.lojoso.sudie.mesh.center.config.SudieBaseConfig;
import org.lojoso.sudie.mesh.center.kernel.client.SudieAIOClient;
import org.lojoso.sudie.mesh.center.kernel.decoder.DgDecoder;

public class SudieAIOServer {

    public SudieAIOServer() {
        this(new SudieBaseConfig() {{
            setPort(60001);
            setClusters("localhost:60002");
        }});
    }

    private void startCluster(SudieBaseConfig config) {
        Thread cluster = new Thread(() -> {
            new SudieAIOClient(config);
        });
        cluster.start();
    }

    public SudieAIOServer(SudieBaseConfig config) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new DgDecoder()).addLast(new DiscardServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 2048)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
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
