package org.lojoso.sudie.mesh.center.kernel.server.strategy;

import io.netty.channel.Channel;
import org.apache.commons.collections4.CollectionUtils;
import org.lojoso.sudie.mesh.center.kernel.data.CoreDataQueue;
import org.lojoso.sudie.mesh.center.kernel.model.Dg;
import org.lojoso.sudie.mesh.center.utils.DgTools;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// 服务队列均衡动作，分散发送
public class ServerPushStrategy implements DgStrategy{

    private List<Dg> target;

    @Override
    public boolean judge(List<Dg> datas) {
        target = datas.stream().filter(e -> Arrays.equals(e.getAfn(), DgTools.SD_AFN_PUSH)).collect(Collectors.toList());
        return CollectionUtils.isNotEmpty(target);
    }

    @Override
    public void doEncode(Channel channel) {
        // todo:触发处理机制
        CoreDataQueue.batchAddRequest(target);
        System.out.printf("current queue size: %s\n", CoreDataQueue.count());
    }
}
