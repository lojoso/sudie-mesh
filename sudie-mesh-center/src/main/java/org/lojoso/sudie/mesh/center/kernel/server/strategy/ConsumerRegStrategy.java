package org.lojoso.sudie.mesh.center.kernel.server.strategy;

import io.netty.channel.Channel;
import org.apache.commons.collections4.CollectionUtils;
import org.lojoso.sudie.mesh.center.kernel.server.ClientCache;
import org.lojoso.sudie.mesh.common.decode.strategy.DgStrategy;
import org.lojoso.sudie.mesh.common.model.Dg;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.lojoso.sudie.mesh.common.config.CommonData.CD_AFN_CLI_REG;

public class ConsumerRegStrategy implements DgStrategy {

    private List<Dg> target;


    @Override
    public boolean judge(List<? extends Dg> datas) {
        target = datas.stream().filter(e -> Arrays.equals(e.getAfn(), CD_AFN_CLI_REG)).collect(Collectors.toList());
        return CollectionUtils.isNotEmpty(target);
    }

    @Override
    public void doEncode(Channel channel) {
        target.forEach(e -> ClientCache.initClient(new String(e.getBody(), StandardCharsets.UTF_8), channel));
    }
}
