package org.lojoso.sudie.mesh.provider;

import org.apache.commons.codec.binary.Hex;
import org.junit.Test;
import org.lojoso.sudie.mesh.common.encode.encoder.ProviderEncoder;
import org.lojoso.sudie.mesh.common.tsvc.TUser;
import org.lojoso.sudie.mesh.common.tsvc.TestService;
import org.lojoso.sudie.mesh.provider.kernel.client.ClusterCache;
import org.lojoso.sudie.mesh.provider.kernel.client.ProviderClient;
import org.lojoso.sudie.mesh.provider.kernel.client.RegHandler;

import java.util.concurrent.locks.LockSupport;

public class TestAppliction {

    @Test
    public void test() throws InterruptedException {


        ClusterCache.serviceMapping.put(TestService.class.getName(), new MyService());
        ProviderClient.startCluster("localhost:60001,localhost:60002");

        ProviderEncoder encoder = new ProviderEncoder();
        RegHandler.regToCluster(encoder.encode(MyService.class));
        LockSupport.park();
    }


    public static class MyService implements TestService{

        @Override
        public void sayHello(String name, String world) {
            System.out.printf("%s say：%s \n", name, world);
        }

        @Override
        public void sayHello(TUser user) {
            System.out.printf("my name is %s \n", user.getUserName());
        }
    }
}
