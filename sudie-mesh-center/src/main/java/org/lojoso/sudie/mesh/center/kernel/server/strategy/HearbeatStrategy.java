package org.lojoso.sudie.mesh.center.kernel.server.strategy;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import org.lojoso.sudie.mesh.common.decode.strategy.DgStrategy;
import org.lojoso.sudie.mesh.common.model.CommonMethod;
import org.lojoso.sudie.mesh.common.model.Dg;

import java.util.Arrays;
import java.util.List;

import static org.lojoso.sudie.mesh.common.config.CommonData.HB_AFN;

// 心跳处理
public class HearbeatStrategy implements DgStrategy {

    @Override
    public boolean judge(List<? extends Dg> datas) {
        return datas.stream().anyMatch(e -> Arrays.equals(e.getAfn(), HB_AFN));
    }

    @Override
    public void doEncode(Channel channel) {
        channel.writeAndFlush(Unpooled.wrappedBuffer(CommonMethod.toHeartbeat()));
    }
}
