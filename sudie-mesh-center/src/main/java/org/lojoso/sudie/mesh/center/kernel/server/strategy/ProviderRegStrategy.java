package org.lojoso.sudie.mesh.center.kernel.server.strategy;

import io.netty.channel.Channel;
import org.apache.commons.collections4.CollectionUtils;
import org.lojoso.sudie.mesh.center.kernel.server.analysis.Analysis;
import org.lojoso.sudie.mesh.center.kernel.server.analysis.registry.RegistryAnalysis;
import org.lojoso.sudie.mesh.center.kernel.server.analysis.registry.RegistryModel;
import org.lojoso.sudie.mesh.center.kernel.service.ServiceCache;
import org.lojoso.sudie.mesh.common.decode.strategy.DgStrategy;
import org.lojoso.sudie.mesh.common.model.Dg;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.lojoso.sudie.mesh.common.model.CommonData.CD_AFN_REG;

public class ProviderRegStrategy implements DgStrategy {

    private List<Dg> target;
    private final Analysis<RegistryModel> analysis = new RegistryAnalysis();


    @Override
    public boolean judge(List<? extends Dg> datas) {
        target = datas.stream().filter(e -> Arrays.equals(e.getAfn(), CD_AFN_REG)).collect(Collectors.toList());
        return CollectionUtils.isNotEmpty(target);
    }

    @Override
    public void doEncode(Channel channel) {
        // 多provider映射
        analysis.analysis(target).forEach(e -> e.getClassNames().forEach(c -> ServiceCache.initService(c, channel)));
    }
}
