package org.lojoso.sudie.mesh.center.kernel.server.strategy;

import io.netty.channel.Channel;
import org.apache.commons.collections4.CollectionUtils;
import org.lojoso.sudie.mesh.center.kernel.consumer.DataConsumer;
import org.lojoso.sudie.mesh.center.kernel.server.ClientCache;
import org.lojoso.sudie.mesh.center.kernel.server.analysis.Analysis;
import org.lojoso.sudie.mesh.center.kernel.server.analysis.request.RequestAnalysis;
import org.lojoso.sudie.mesh.center.kernel.server.analysis.request.RequestModel;
import org.lojoso.sudie.mesh.common.data.CoreDataQueue;
import org.lojoso.sudie.mesh.common.decode.strategy.DgStrategy;
import org.lojoso.sudie.mesh.common.model.Dg;


import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.lojoso.sudie.mesh.common.config.CommonData.*;

// 客户端请求
public class ClientStrategy implements DgStrategy {

    private List<Dg> target;
    private final Analysis<RequestModel> analysis = new RequestAnalysis();


    @Override
    public boolean judge(List<? extends Dg> datas) {
        // 处理数据请求
        target = datas.stream().filter(e -> Arrays.equals(e.getAfn(), CD_AFN)).collect(Collectors.toList());
        return CollectionUtils.isNotEmpty(target);
    }

    @Override
    public void doEncode(Channel channel) {
        // type[0]: client -> server -> server 二次负载分发，目前看没有太大优势，consumer层进行负载好一些。
        // target.forEach((e) -> Cluster.randomChannel().write(Unpooled.wrappedBuffer(e.combine(CommonData.SD_AFN_PUSH))));
        // Cluster.flush();
        List<RequestModel> requests = analysis.analysis(target);
        requests.forEach(e -> e.setChannelIndex(ClientCache.getIndex(channel)));
        target.forEach((e) -> CoreDataQueue.batchAddRequest(requests));
        DataConsumer.requestProcess();
    }
}
