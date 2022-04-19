package org.lojoso.sudie.mesh.common.tsvc;

public interface TestService extends BaseService {

    String sayHello(String name, String world);

    void sayHello(TUser user);

    TUser userSayHello(String user, Integer seq);
}
