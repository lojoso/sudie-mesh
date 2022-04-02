package org.lojoso.sudie.mesh.center.kernel.consumer;

import org.lojoso.sudie.mesh.center.kernel.data.CoreDataQueue;
import org.lojoso.sudie.mesh.center.kernel.model.Dg;

import java.util.List;

public class DataConsumer {

    // 处理请求消费
    public static void requestProcess(){
        List<Dg> target = CoreDataQueue.getRequest(20);
        System.out.println(target);
        if(CoreDataQueue.count() != 0){
            requestProcess();
        }
    }
}
