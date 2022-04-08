package org.lojoso.sudie.mesh.provider.kernel.client;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import org.lojoso.sudie.mesh.common.encode.encoder.ProviderEncoder;
import org.lojoso.sudie.mesh.common.model.CommonMethod;
import org.lojoso.sudie.mesh.provider.kernel.data.RequestModel;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.netty.handler.timeout.IdleState.WRITER_IDLE;

public class DiscardClientHandler extends ChannelInboundHandlerAdapter {

    private String server;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    private Class<?>[] classes;
    private ProviderEncoder encoder;

    public DiscardClientHandler(){
    }

    public Class<?>[] getClasses() {
        return classes;
    }

    public void setClasses(Class<?>[] classes) {
        this.classes = classes;
    }

    public ProviderEncoder getEncoder() {
        return encoder;
    }

    public void setEncoder(ProviderEncoder encoder) {
        this.encoder = encoder;
    }

    public void setServer(String server) {
        this.server = server;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelRegistered();
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelUnregistered();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ClusterCache.clusterMapping.put(server, ctx.channel());
        Arrays.stream(classes).forEach(e -> RegHandler.regToCluster(ctx.channel(), encoder.encode(e)));
        System.out.printf("server: [ %s ] connected ... \n", server);
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.printf("server: [ %s ] disconnected ... \n", server);
        ClusterCache.clusterMapping.remove(server);
        ProviderClient.connect(ClusterCache.clusters.remove(ctx.channel().id()), null, classes);
        ctx.fireChannelInactive();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            List<RequestModel> data = (List<RequestModel>) msg;
            data.forEach(d -> executorService.submit(() -> {
                try {
                    System.out.printf("from [ %s ] ", ClusterCache.clusters.get(d.getId()));
                    d.getTargetMethod().invoke(d.getTarget(), d.getTargetParams());
                } catch (InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }));
        } finally {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelReadComplete();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            if (Objects.equals(((IdleStateEvent) evt).state(), WRITER_IDLE)) {
                ctx.channel().writeAndFlush(Unpooled.wrappedBuffer(CommonMethod.toHeartbeat()));
            }
        }else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelWritabilityChanged();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
    }
}
