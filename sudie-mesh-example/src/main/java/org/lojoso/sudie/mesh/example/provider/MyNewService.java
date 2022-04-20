package org.lojoso.sudie.mesh.example.provider;

import org.lojoso.sudie.mesh.common.tsvc.NewService;
import org.lojoso.sudie.mesh.common.tsvc.TestService;
import org.lojoso.sudie.mesh.consumer.kernel.proxy.ConsumerProxy;

public class MyNewService implements NewService {

    @Override
    public String callService(String username) {
        System.out.println("..... do something ....");
        String result = ConsumerProxy.getProxy(TestService.class).sayHello("我", String.valueOf(Math.random()));
        return String.format("from other provider : %s", result);
    }
}
