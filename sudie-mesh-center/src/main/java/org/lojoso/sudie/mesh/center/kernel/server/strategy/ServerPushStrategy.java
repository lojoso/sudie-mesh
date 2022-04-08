package org.lojoso.sudie.mesh.center.kernel.server.strategy;

import io.netty.channel.Channel;
import org.apache.commons.collections4.CollectionUtils;
import org.lojoso.sudie.mesh.center.kernel.consumer.DataConsumer;
import org.lojoso.sudie.mesh.center.kernel.server.analysis.Analysis;
import org.lojoso.sudie.mesh.center.kernel.server.analysis.request.RequestAnalysis;
import org.lojoso.sudie.mesh.center.kernel.server.analysis.request.RequestModel;
import org.lojoso.sudie.mesh.common.data.CoreDataQueue;
import org.lojoso.sudie.mesh.common.decode.strategy.DgStrategy;
import org.lojoso.sudie.mesh.common.decode.utils.DgTools;
import org.lojoso.sudie.mesh.common.model.Dg;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.lojoso.sudie.mesh.common.model.CommonData.*;

// 服务队列均衡动作，分散发送
public class ServerPushStrategy implements DgStrategy {

    private List<Dg> target;
    private final Analysis<RequestModel> analysis = new RequestAnalysis();

    @Override
    public boolean judge(List<? extends Dg> datas) {
        target = datas.stream().filter(e -> Arrays.equals(e.getAfn(), SD_AFN_PUSH)).collect(Collectors.toList());
        return CollectionUtils.isNotEmpty(target);
    }

    @Override
    public void doEncode(Channel channel) {
        // todo:触发处理机制
        CoreDataQueue.batchAddRequest(analysis.analysis(target));
//        System.out.printf("current queue size: %s\n", CoreDataQueue.count());
        DataConsumer.requestProcess();
    }
}
