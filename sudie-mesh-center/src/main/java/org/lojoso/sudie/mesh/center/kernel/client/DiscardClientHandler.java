package org.lojoso.sudie.mesh.center.kernel.client;

import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.timeout.IdleStateEvent;
import org.lojoso.sudie.mesh.center.kernel.model.Dg;
import org.lojoso.sudie.mesh.center.utils.DgTools;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import static io.netty.handler.timeout.IdleState.READER_IDLE;

public class DiscardClientHandler extends ChannelInboundHandlerAdapter {

    private ConcurrentHashMap<ChannelId, String> clusters;
    // 心跳标志
    private final AtomicBoolean inActive = new AtomicBoolean(false);

    public DiscardClientHandler(){

    }

    public DiscardClientHandler(ConcurrentHashMap<ChannelId, String> clusters){
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
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("disconnected");
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
            if (Objects.equals(((IdleStateEvent) evt).state(), READER_IDLE)) {
                if (inActive.get()) {
                    ctx.close();
                } else {
                    ctx.channel().writeAndFlush(Unpooled.wrappedBuffer(DgTools.toHeartbeat()));
                    inActive.set(true);
                }
            }
        }
        super.userEventTriggered(ctx, evt);
        ctx.fireUserEventTriggered(evt);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelWritabilityChanged();
    }
}
