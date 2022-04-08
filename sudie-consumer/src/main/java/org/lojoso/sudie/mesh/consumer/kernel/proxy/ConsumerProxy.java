package org.lojoso.sudie.mesh.consumer.kernel.proxy;

import io.netty.buffer.Unpooled;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.lojoso.sudie.mesh.consumer.kernel.client.Cluster;

import java.lang.reflect.Method;

public class ConsumerProxy {

    @SuppressWarnings("unchecked")
    public static <T> T getProxy(Class<T> clazz){
        Enhancer e = new Enhancer();
        e.setInterfaces(new Class[]{clazz});
        e.setCallback(new ConsumerInterceptor());
        return  (T) e.create();
    }

    private static class ConsumerInterceptor implements MethodInterceptor {
        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) {
            Cluster.randomChannel().writeAndFlush(Unpooled.wrappedBuffer(Cluster.encoder.encode(method, objects)));
            return null;
        }
    }

}
