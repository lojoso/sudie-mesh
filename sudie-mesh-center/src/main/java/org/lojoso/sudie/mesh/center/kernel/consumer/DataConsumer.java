package org.lojoso.sudie.mesh.center.kernel.consumer;

import org.lojoso.sudie.mesh.common.data.CoreDataQueue;
import org.lojoso.sudie.mesh.common.model.Dg;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DataConsumer {

    static AtomicInteger count = new AtomicInteger(0);
    // 处理请求消费
    public static void requestProcess(){
        List<? super Dg> target = CoreDataQueue.getRequest(1000);
        //todo: 需要服务声明表格(class -> channel)
        count.addAndGet(target.size());
        System.out.println(count.get());
        if(CoreDataQueue.count() != 0){
            requestProcess();
        }
    }
}
