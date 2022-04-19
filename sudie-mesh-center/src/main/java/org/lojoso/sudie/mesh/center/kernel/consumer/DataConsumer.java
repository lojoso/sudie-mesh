package org.lojoso.sudie.mesh.center.kernel.consumer;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.apache.commons.codec.binary.Hex;
import org.lojoso.sudie.mesh.center.kernel.server.analysis.request.RequestModel;
import org.lojoso.sudie.mesh.center.kernel.server.ServiceCache;
import org.lojoso.sudie.mesh.common.data.CoreDataQueue;
import org.lojoso.sudie.mesh.common.model.Dg;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class DataConsumer {

    static AtomicInteger count = new AtomicInteger(0);
    // 处理请求消费
    public static void requestProcess(){
        List<? super Dg> target = CoreDataQueue.getRequest(1000);
        target.stream().map(e -> (RequestModel) e).forEach(e -> {
            Optional.ofNullable(ServiceCache.randomChannel(e.getClassName())).map(c -> {
                        ChannelFuture channelFuture = c.writeAndFlush(Unpooled.wrappedBuffer(e.rebuild()));
                        channelFuture.addListener((ChannelFutureListener) future -> {
                            if(!future.isSuccess()){
                                System.out.println(future.cause().getMessage());
                            }
                        });
                        return channelFuture;
                    })
                    .orElseGet(() -> {
//                        CoreDataQueue.addRequest(e);
                        return null;
                    });
        });

        count.addAndGet(target.size());
        if(CoreDataQueue.count() != 0){
            requestProcess();
        }
    }
}
