package org.lojoso.sudie.mesh.center.kernel.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;

import org.lojoso.sudie.mesh.common.data.BrokenDgPool;
import org.lojoso.sudie.mesh.common.decode.strategy.DgStrategy;
import org.lojoso.sudie.mesh.common.model.Dg;

import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static io.netty.handler.timeout.IdleState.READER_IDLE;

public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

    public DiscardServerHandler(){
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        // 通道绑定
        ctx.fireChannelRegistered();
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        // 通道解绑
        ctx.fireChannelUnregistered();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 通道初始化
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 通道销毁
        ctx.fireChannelInactive();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            // ByteBufUtil, Unpooled
            List<Dg> data = (List<Dg>) msg;
            ServiceLoader.load(DgStrategy.class).forEach(e -> e.apply(data, ctx.channel()));
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }


    private void recheckData(List<Dg> data){

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if ((evt instanceof IdleStateEvent) && Objects.equals(((IdleStateEvent) evt).state(), READER_IDLE)) {
            // 关闭信道
            ctx.close();
        }else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
