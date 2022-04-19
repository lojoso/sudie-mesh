package org.lojoso.sudie.mesh.provider;

import org.junit.Test;
import org.lojoso.sudie.mesh.common.tsvc.TUser;
import org.lojoso.sudie.mesh.common.tsvc.TestService;
import org.lojoso.sudie.mesh.provider.kernel.client.ClusterCache;
import org.lojoso.sudie.mesh.provider.kernel.client.ProviderClient;

import java.util.concurrent.locks.LockSupport;

public class TestAppliction {

    @Test
    public void test() throws InterruptedException {


        ClusterCache.serviceMapping.put(TestService.class.getName(), new MyService());
        ProviderClient.startCluster("localhost:60001,localhost:60002", MyService.class);
        LockSupport.park();
    }


    public static class MyService implements TestService{

        @Override
        public String sayHello(String name, String world) {
            return String.format("%s say：%s \n", name, world);
        }

        @Override
        public void sayHello(TUser user) {
            System.out.printf("my name is %s \n", user.getUserName());
        }

        @Override
        public TUser userSayHello(String user, Integer seq) {
            TUser u = new TUser();
            u.setUserName(user);
            u.setSeq(seq);
            return u;
        }
    }
}
