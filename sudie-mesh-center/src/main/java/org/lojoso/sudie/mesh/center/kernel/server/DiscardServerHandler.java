package org.lojoso.sudie.mesh.center.kernel.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import org.lojoso.sudie.mesh.center.kernel.model.Dg;
import org.lojoso.sudie.mesh.center.utils.DgTools;

import java.util.List;
import java.util.Objects;

import static io.netty.handler.timeout.IdleState.READER_IDLE;

public class DiscardServerHandler extends ChannelInboundHandlerAdapter {


    public DiscardServerHandler(){

    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        // 通道绑定
        ctx.fireChannelRegistered();
        System.out.println("1");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        // 通道解绑
        ctx.fireChannelUnregistered();
        System.out.println("2");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 通道初始化
        ctx.fireChannelActive();
        System.out.println("3");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 通道销毁
        ctx.fireChannelInactive();
        System.out.println("4");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            // ByteBufUtil, Unpooled
            List<Dg> data = (List<Dg>) msg;
            ctx.channel().writeAndFlush(Unpooled.wrappedBuffer(DgTools.toHeartbeat()));
        }finally {
            ReferenceCountUtil.release(msg);
        }
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
