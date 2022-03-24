package org.lojoso.sudie.mesh.center.kernel.server.strategy;

import io.netty.channel.Channel;
import org.lojoso.sudie.mesh.center.kernel.model.Dg;

import java.util.List;

//报文解析策略
public interface DgStrategy {

    // 判断报文是否有符合
    boolean judge(List<Dg> datas);

    // 解析操作
    void doEncode(Channel channel);

    default void apply(List<Dg> datas, Channel channel) {
        if (judge(datas)) {
            doEncode(channel);
        }
    }


}
