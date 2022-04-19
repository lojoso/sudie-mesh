package org.lojoso.sudie.mesh.center.kernel.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.lojoso.sudie.mesh.center.config.SudieBaseConfig;
import org.lojoso.sudie.mesh.center.kernel.client.SudieAIOClient;
import org.lojoso.sudie.mesh.common.config.DefaultConfig;
import org.lojoso.sudie.mesh.common.decode.decoder.DgDecoder;

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
                    .childOption(NioChannelOption.SO_KEEPALIVE, true)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            // 60s 无读取-关闭连接
                            ch.pipeline().addLast(new IdleStateHandler(DefaultConfig.SERVER_HB_SECONDS, 0,0, TimeUnit.SECONDS))
                                    .addLast(new LengthFieldBasedFrameDecoder(DefaultConfig.LENBASED_DE_MAX_LEN, DefaultConfig.LENBASED_DE_OFFSET, DefaultConfig.LENBASED_DE_LEN,DefaultConfig.LENBASED_DE_LEN_ADJUST,0))
                                    .addLast(new DgDecoder()).addLast(new DiscardServerHandler());
                        }
                    });
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
