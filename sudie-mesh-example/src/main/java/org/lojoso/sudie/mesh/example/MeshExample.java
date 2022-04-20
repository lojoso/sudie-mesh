package org.lojoso.sudie.mesh.example;

import org.lojoso.sudie.mesh.common.tsvc.NewService;
import org.lojoso.sudie.mesh.common.tsvc.TestService;
import org.lojoso.sudie.mesh.common.utils.FlagArgs;
import org.lojoso.sudie.mesh.consumer.kernel.client.ConsumerClient;
import org.lojoso.sudie.mesh.example.consumer.ExampleConsumer;
import org.lojoso.sudie.mesh.example.provider.MyNewService;
import org.lojoso.sudie.mesh.provider.MeshProvider;
import org.lojoso.sudie.mesh.provider.kernel.client.ClusterCache;
import org.lojoso.sudie.mesh.provider.kernel.client.ProviderClient;

import java.util.concurrent.locks.LockSupport;

public class MeshExample {

    public static void main(String[] args) throws InterruptedException {
        ClusterCache.serviceMapping.put(NewService.class.getName(), new MyNewService());
        ProviderClient.startCluster("localhost:60001,localhost:60002", MyNewService.class);

        ConsumerClient.startCluster(FlagArgs.getValue(args, "-s", "localhost:60001"));

        ExampleConsumer example = new ExampleConsumer();
        example.callMethod();

        LockSupport.park();

    }
}
