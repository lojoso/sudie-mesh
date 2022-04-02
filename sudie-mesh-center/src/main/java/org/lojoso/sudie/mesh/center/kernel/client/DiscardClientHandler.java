package org.lojoso.sudie.mesh.center.kernel.client;

import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateEvent;
import org.lojoso.sudie.mesh.center.kernel.model.Dg;
import org.lojoso.sudie.mesh.center.utils.DgTools;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicBoolean;

import static io.netty.handler.timeout.IdleState.READER_IDLE;
import static io.netty.handler.timeout.IdleState.WRITER_IDLE;

public class DiscardClientHandler extends ChannelInboundHandlerAdapter {

    private String server;
    private ConcurrentHashMap<ChannelId, String> clusters;
    // 心跳标志
    private final AtomicBoolean inActive = new AtomicBoolean(false);

    public DiscardClientHandler(){
    }

    public void setServer(String server) {
        this.server = server;
    }

    public DiscardClientHandler(ConcurrentHashMap<ChannelId, String> clusters){
        this.clusters = clusters;
    }

    public DiscardClientHandler(String server, ConcurrentHashMap<ChannelId, String> clusters){
        this.server = server;
        this.clusters = clusters;
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
        Cluster.clusterMapping.put(server, ctx.channel());
        System.out.printf("server: [ %s ] connected ... \n", server);
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.printf("server: [ %s ] disconnected ... \n", server);
        Cluster.clusterMapping.remove(server);
        String server = ClusterCache.clusters.get(ctx.channel().id());
        ClusterKernelPool.connect(server);
        ctx.fireChannelInactive();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            inActive.set(false);
            List<Dg> data = (List<Dg>) msg;

            System.out.printf("%s :=> %s \n", clusters.get(ctx.channel().id()), data);
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
                ctx.channel().writeAndFlush(Unpooled.wrappedBuffer(DgTools.toHeartbeat()));
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
        System.out.println("exception");
//        ctx.fireExceptionCaught(cause);
    }
}
