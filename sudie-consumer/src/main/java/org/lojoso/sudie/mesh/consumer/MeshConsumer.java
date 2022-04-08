package org.lojoso.sudie.mesh.consumer;

import org.lojoso.sudie.mesh.common.utils.FlagArgs;
import org.lojoso.sudie.mesh.consumer.kernel.client.ConsumerClient;

import java.util.concurrent.locks.LockSupport;

public class MeshConsumer {

    public static void main(String[] args) throws InterruptedException {
        ConsumerClient.startCluster(FlagArgs.getValue(args, "-s", "localhost:60001"));
        LockSupport.park();
    }
}
