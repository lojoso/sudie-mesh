package org.lojoso.sudie.mesh.center.kernel.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.util.internal.shaded.org.jctools.queues.MpscArrayQueue;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class ServiceCache {

    private static final Object lock = new Object();
    private static final Map<String, LinkedList<Channel>> services = new HashMap<>();
    private static final Map<ChannelId, String> serviceMapping = new HashMap<>();

    public static Channel randomChannel(String service){
        LinkedList<Channel> linkedList = services.get(service);
        return Optional.ofNullable(linkedList).filter(CollectionUtils::isNotEmpty).map(l -> {
            l.forEach(c -> Optional.of(c).filter(((Predicate<? super Channel>) Channel::isActive).negate())
                    .ifPresent(l::remove));
            return l.get((int) (Math.random() * l.size()));
        }).orElse(null);
    }

    public static void initService(String clazz, Channel channel){
        synchronized (lock){
            services.compute(clazz, (k,v) -> {
                serviceMapping.put(channel.id(), clazz);
                if(Objects.isNull(v)){
                    return new LinkedList<Channel>(){{add(channel);}};
                }
                v.add(channel);
                return v;
            });
        }
    }

    public static void removeService(Channel channel){
        synchronized (lock){
            Optional.ofNullable(serviceMapping.remove(channel.id())).ifPresent(clazz -> services.get(clazz).remove(channel));
        }
    }

}
