package org.lojoso.sudie.mesh.center;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.lojoso.sudie.mesh.center.config.SudieBaseConfig;
import org.lojoso.sudie.mesh.center.kernel.server.SudieAIOServer;
import org.lojoso.sudie.mesh.common.utils.FlagArgs;


import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

public class MeshCenterApplication {

    public static void main(String[] args) {
        SudieBaseConfig config = new SudieBaseConfig(FlagArgs.getValue(args, "-p", "60001"),
                FlagArgs.getValue(args, "-s", "localhost:60001"));
//        config.setRetry(10);
        SudieAIOServer.start(config);
    }

    public static void test() {
        Enhancer e = new Enhancer();
        e.setInterfaces(new Class[]{TestInterface.class});
        e.setCallback(new MyMethodInterceptor());
        TestInterface test = (TestInterface) e.create();
        test.doAction();

    }

    public interface TestInterface {
        void doAction();
    }

    public static class MyMethodInterceptor implements MethodInterceptor {

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

            Optional.of(TestInterface.class.getMethod("doAction")).filter(m -> Objects.equals(m, method))
                    .ifPresent(e -> System.out.println("call doAction"));
            return null;
        }
    }
}
