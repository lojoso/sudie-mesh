package org.lojoso.sudie.mesh.center.kernel.consumer;

import org.lojoso.sudie.mesh.center.kernel.data.CoreDataQueue;
import org.lojoso.sudie.mesh.center.kernel.model.Dg;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DataConsumer {

    static AtomicInteger count = new AtomicInteger(0);
    // 处理请求消费
    public static void requestProcess(){
        List<Dg> target = CoreDataQueue.getRequest(1000);
        count.addAndGet(target.size());
        System.out.println(count.get());
        if(CoreDataQueue.count() != 0){
            requestProcess();
        }
    }
}
