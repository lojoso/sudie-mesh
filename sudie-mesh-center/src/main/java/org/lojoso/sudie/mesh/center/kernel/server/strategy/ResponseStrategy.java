package org.lojoso.sudie.mesh.center.kernel.server.strategy;

import io.netty.channel.Channel;
import org.apache.commons.collections4.CollectionUtils;
import org.lojoso.sudie.mesh.common.decode.strategy.DgStrategy;
import org.lojoso.sudie.mesh.common.model.Dg;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.lojoso.sudie.mesh.common.model.CommonData.CD_AFN_RES;

public class ResponseStrategy implements DgStrategy {

    private List<Dg> target;

    @Override
    public boolean judge(List<? extends Dg> datas) {
        target = datas.stream().filter(e -> Arrays.equals(e.getAfn(), CD_AFN_RES)).collect(Collectors.toList());
        return CollectionUtils.isNotEmpty(target);
    }

    @Override
    public void doEncode(Channel channel) {
        // 接收返回值，需要回源client端
        System.out.println(target);
    }
}
