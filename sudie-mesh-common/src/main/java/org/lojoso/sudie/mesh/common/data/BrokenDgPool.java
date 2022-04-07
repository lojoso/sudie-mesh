package org.lojoso.sudie.mesh.common.data;

import io.netty.channel.ChannelId;
import org.apache.commons.codec.binary.Hex;
import org.lojoso.sudie.mesh.common.model.Dg;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

// 断帧池
public abstract class BrokenDgPool {

    protected final ConcurrentHashMap<ChannelId, ArrayDeque<Dg>> withHead;

    public BrokenDgPool() {
        this.withHead = new ConcurrentHashMap<>();
        ScheduledExecutorService service = Executors.newScheduledThreadPool(2);
        service.schedule(() -> this.withHead.entrySet().forEach(e -> {
            System.out.printf("[%s] data: %s\n", e.getKey().asShortText(), e.getValue().stream().map(d -> Hex.encodeHexString(d.rebuild())).collect(Collectors.joining()));
        }), 30, TimeUnit.SECONDS);
    }

    public abstract void joinPoolHandler(Dg data, ChannelId id, List<Dg> array);

    private void initQueue(ChannelId id) {
        this.withHead.putIfAbsent(id, new ArrayDeque<>());
    }

    public List<Dg> combineDg(List<Dg> datas, ChannelId id) {
        initQueue(id);
        List<Dg> result = new ArrayList<>();
        datas.stream().filter(Dg::getBroken).forEach(d -> this.joinPoolHandler(d, id, result));
        return result;
    }

}
