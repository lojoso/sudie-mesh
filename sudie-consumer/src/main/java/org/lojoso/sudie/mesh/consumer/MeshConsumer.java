package org.lojoso.sudie.mesh.consumer;

import org.lojoso.sudie.mesh.common.tsvc.TUser;
import org.lojoso.sudie.mesh.common.tsvc.TestService;
import org.lojoso.sudie.mesh.common.utils.FlagArgs;
import org.lojoso.sudie.mesh.consumer.kernel.client.ConsumerClient;
import org.lojoso.sudie.mesh.consumer.kernel.proxy.ConsumerProxy;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class MeshConsumer {

    public static void main(String[] args) throws InterruptedException {
        ConsumerClient.startCluster(FlagArgs.getValue(args, "-s", "localhost:60001"));
        ConsumerProxy.getProxy(TestService.class).sayHello(new TUser("123"));
        Executors.newScheduledThreadPool(10).scheduleWithFixedDelay(() -> {
            ConsumerProxy.getProxy(TestService.class).sayHello("我", String.valueOf(Math.random()));
        }, 1, 10, TimeUnit.MILLISECONDS);
        LockSupport.park();

    }
}
