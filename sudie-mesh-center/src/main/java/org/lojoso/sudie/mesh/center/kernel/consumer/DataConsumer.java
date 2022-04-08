package org.lojoso.sudie.mesh.center.kernel.consumer;

import io.netty.buffer.Unpooled;
import org.lojoso.sudie.mesh.center.kernel.server.analysis.request.RequestModel;
import org.lojoso.sudie.mesh.center.kernel.service.ServiceCache;
import org.lojoso.sudie.mesh.common.data.CoreDataQueue;
import org.lojoso.sudie.mesh.common.model.Dg;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class DataConsumer {

    static AtomicInteger count = new AtomicInteger(0);
    // 处理请求消费
    public static void requestProcess(){
        List<? super Dg> target = CoreDataQueue.getRequest(1000);
        target.stream().map(e -> (RequestModel) e).forEach(e -> {
            Optional.ofNullable(ServiceCache.cache.get(e.getClassName())).map(c -> c.writeAndFlush(Unpooled.wrappedBuffer(e.rebuild())))
                    .orElseGet(() -> {
//                        CoreDataQueue.addRequest(e);
                        return null;
                    });
        });

        count.addAndGet(target.size());
        System.out.println(count.get());
        if(CoreDataQueue.count() != 0){
            requestProcess();
        }
    }
}
