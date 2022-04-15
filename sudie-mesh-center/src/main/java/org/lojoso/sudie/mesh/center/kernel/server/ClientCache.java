package org.lojoso.sudie.mesh.center.kernel.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientCache {


    private static final AtomicInteger index = new AtomicInteger(0);
    // uuid -> channelId
    private static final Map<ChannelId, String> idMapping = new HashMap<>();
    // uuid -> index
    private static final Map<String, Integer> clients = new HashMap<>();
    // index -> channel
    private static final Map<Integer, Channel> clientsMapping = new HashMap<>();

    public static Integer getIndex(Channel channel){
        return clients.get(idMapping.get(channel.id()));
    }

    public static void initClient(String uuid, Channel channel){
        synchronized (index){
            idMapping.put(channel.id(), uuid);
            clients.put(uuid, index.incrementAndGet());
            clientsMapping.put(index.get(), channel);
        }
    }

    public static void removeClient(Channel channel){
        synchronized (index){
            String uuid = idMapping.remove(channel.id());
            Integer index = clients.remove(uuid);
            clientsMapping.remove(index);
        }
    }


}
