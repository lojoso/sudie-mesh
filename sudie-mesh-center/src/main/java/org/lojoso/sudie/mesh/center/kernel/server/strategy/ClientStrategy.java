package org.lojoso.sudie.mesh.center.kernel.server.strategy;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import org.apache.commons.collections4.CollectionUtils;
import org.lojoso.sudie.mesh.center.kernel.client.Cluster;
import org.lojoso.sudie.mesh.center.kernel.model.Dg;
import org.lojoso.sudie.mesh.center.utils.DgTools;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// 客户端请求
public class ClientStrategy implements DgStrategy{

    private List<Dg> target;

    @Override
    public boolean judge(List<Dg> datas) {
        // 处理数据请求
        target = datas.stream().filter(e -> Arrays.equals(e.getAfn(), DgTools.CD_AFN)).collect(Collectors.toList());
        return CollectionUtils.isNotEmpty(target);
    }

    @Override
    public void doEncode(Channel channel) {
        target.forEach((e) -> Cluster.randomChannel().write(Unpooled.wrappedBuffer(e.combine(DgTools.SD_AFN_PUSH))));
        Cluster.flush();
    }
}
