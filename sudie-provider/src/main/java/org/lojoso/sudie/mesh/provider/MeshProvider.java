package org.lojoso.sudie.mesh.provider;

import org.lojoso.sudie.mesh.common.tsvc.TUser;
import org.lojoso.sudie.mesh.common.tsvc.TestService;
import org.lojoso.sudie.mesh.provider.kernel.client.ClusterCache;
import org.lojoso.sudie.mesh.provider.kernel.client.ProviderClient;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class MeshProvider {

    public static void main(String[] args) throws InterruptedException {
        ClusterCache.serviceMapping.put(TestService.class.getName(), new MyService());
        ProviderClient.startCluster("localhost:60001,localhost:60002", MyService.class);
        LockSupport.park();
    }

    public static class MyService implements TestService {

        @Override
        public String sayHello(String name, String world) {
            throw new RuntimeException("异常了");
//            return String.format("%s say：%s", name, world);
        }

        @Override
        public void sayHello(TUser user) {
            System.out.printf("my name is %s \n", user.getUserName());
        }

        @Override
        public TUser userSayHello(String user, int seq) {
            TUser u = new TUser();
            u.setUserName(user);
            u.setSeq(seq);
            return u;
        }
    }
}
