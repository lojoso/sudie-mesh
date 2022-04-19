package org.lojoso.sudie.mesh.consumer.kernel.proxy;

import io.netty.buffer.Unpooled;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.lojoso.sudie.mesh.common.encode.config.FastSerialization;
import org.lojoso.sudie.mesh.common.model.CommonState;
import org.lojoso.sudie.mesh.consumer.kernel.client.Cluster;
import org.lojoso.sudie.mesh.consumer.kernel.client.ClusterCache;
import org.lojoso.sudie.mesh.consumer.kernel.model.ResponseModel;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;

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
            int seq = Cluster.seq.getAndAdd(1);
            Cluster.randomChannel().writeAndFlush(Unpooled.wrappedBuffer(Cluster.reqEncoder.encode(method, objects, seq)));
            ResponseModel model = ClusterCache.waiting(seq, method.getReturnType());
            return Optional.ofNullable(model).map(ResponseModel::getState).map(s -> {
                if(Objects.equals(s, CommonState.SUCCESS_NO_RES)){
                    return null;
                }else if(Objects.equals(s, CommonState.EXCEPTION)){
                    throw new RuntimeException(FastSerialization.getFstConfig(String.class).get().asObject(model.getResponse()).toString());
                }else {
                    return FastSerialization.getFstConfig(model.getResponseType()).get().asObject(model.getResponse());
                }
            }).orElseThrow(() -> new RuntimeException("TIMEOUT"));
        }
    }

}
