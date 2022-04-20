package org.lojoso.sudie.mesh.example.consumer;

import org.lojoso.sudie.mesh.common.tsvc.NewService;
import org.lojoso.sudie.mesh.consumer.kernel.proxy.ConsumerProxy;

public class ExampleConsumer {

    public void callMethod(){
        System.out.println(ConsumerProxy.getProxy(NewService.class).callService("hi i'm helloword"));
    }
}
