package org.lojoso.sudie.mesh.consumer;

import org.junit.Test;
import org.lojoso.sudie.mesh.consumer.kernel.client.ConsumerClient;
import org.lojoso.sudie.mesh.consumer.kernel.proxy.ConsumerProxy;

import java.util.concurrent.locks.LockSupport;

public class TestApplication {


    @Test
    public void testSendRequest() throws InterruptedException {

        ConsumerClient.startCluster("localhost:60001");
        ConsumerProxy.getProxy(TestService.class).sayHello("123","444");
        LockSupport.park();
    }

}
