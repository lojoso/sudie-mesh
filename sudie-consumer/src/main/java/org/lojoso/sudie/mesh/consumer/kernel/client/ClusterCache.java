package org.lojoso.sudie.mesh.consumer.kernel.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import org.lojoso.sudie.mesh.consumer.kernel.decoder.ResponseDecoder;
import org.lojoso.sudie.mesh.consumer.kernel.model.ResponseModel;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ClusterCache {

    // Channel缓存
    public static final ConcurrentHashMap<ChannelId, String> clusters = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<String, Channel> clusterMapping = new ConcurrentHashMap<>();

    public static final ReentrantLock lock = new ReentrantLock();
    public static final ConcurrentHashMap<Integer, Condition> waitQueue = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<Integer, Class<?>> resTypeMapping = new ConcurrentHashMap<>();
    public static final ConcurrentHashMap<Integer, ResponseModel> resMapping = new ConcurrentHashMap<>();

    public static ResponseModel waiting(Integer key, Class<?> type){
        Condition resultWait = lock.newCondition();
        waitQueue.put(key, resultWait);
        resTypeMapping.put(key, type);
        try {
            lock.lock();
            resultWait.await(30, TimeUnit.SECONDS);
            ResponseModel model = resMapping.remove(key);
            model.setResponseType(resTypeMapping.remove(key));
            return model;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }finally {
            lock.unlock();
        }
    }

    public static void release(Integer key, ResponseModel res){
        try {
            lock.lock();
            ClusterCache.resMapping.put(key, res);
            waitQueue.get(key).signalAll();
        }finally {
            lock.unlock();
        }
    }

}
