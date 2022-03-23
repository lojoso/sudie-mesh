package org.lojoso.sudie.mesh.center.kernel.data;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// 请求数据核心
public class CoreDataQueue {

    private static ConcurrentLinkedDeque<String> dQueue = new ConcurrentLinkedDeque<>();

    public void addRequest(String data) {
        dQueue.add(data);
    }

    public List<String> getRequest(int size) {
        return IntStream.range(0, size).boxed().map(e -> dQueue.remove()).collect(Collectors.toList());
    }
}
