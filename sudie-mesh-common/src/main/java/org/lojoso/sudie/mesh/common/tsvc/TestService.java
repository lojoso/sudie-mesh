package org.lojoso.sudie.mesh.common.tsvc;

public interface TestService extends BaseService {

    void sayHello(String name, String world);

    void sayHello(TUser user);
}
