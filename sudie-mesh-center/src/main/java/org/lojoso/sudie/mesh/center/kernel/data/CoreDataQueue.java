package org.lojoso.sudie.mesh.center.kernel.data;

import org.lojoso.sudie.mesh.center.kernel.model.Dg;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// 请求核心队列
public class CoreDataQueue {

    private static final ConcurrentLinkedDeque<Dg> dQueue = new ConcurrentLinkedDeque<>();

    public static int count(){
        return dQueue.size();
    }

    public static void addRequest(Dg data) {
        dQueue.add(data);
    }

    public static void batchAddRequest(List<Dg> data) {
        dQueue.addAll(data);
    }

    public static List<Dg> getRequest(int size) {
        return IntStream.range(0, size).boxed().map(e -> dQueue.remove()).collect(Collectors.toList());
    }
}
