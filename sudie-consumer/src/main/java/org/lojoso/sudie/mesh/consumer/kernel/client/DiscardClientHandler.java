package org.lojoso.sudie.mesh.consumer.kernel.client;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import org.lojoso.sudie.mesh.common.model.CommonMethod;
import org.lojoso.sudie.mesh.common.model.Dg;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import static io.netty.handler.timeout.IdleState.WRITER_IDLE;

public class DiscardClientHandler extends ChannelInboundHandlerAdapter {

    private String server;

    public DiscardClientHandler(){
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
        System.out.printf("server: [ %s ] connected ... \n", server);
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.printf("server: [ %s ] disconnected ... \n", server);
        ClusterCache.clusterMapping.remove(server);
        ConsumerClient.connect(ClusterCache.clusters.remove(ctx.channel().id()), null);
        ctx.fireChannelInactive();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
//            List<Dg> data = (List<Dg>) msg;
//            System.out.printf("%s :=> %s \n", ClusterCache.clusters.get(ctx.channel().id()), data);
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
