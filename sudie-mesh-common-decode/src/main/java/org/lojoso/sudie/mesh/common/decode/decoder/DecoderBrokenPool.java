package org.lojoso.sudie.mesh.common.decode.decoder;

import io.netty.channel.ChannelId;
import org.apache.commons.codec.binary.Hex;
import org.lojoso.sudie.mesh.common.data.BrokenDgPool;
import org.lojoso.sudie.mesh.common.decode.utils.DgTools;
import org.lojoso.sudie.mesh.common.model.Dg;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

public class DecoderBrokenPool extends BrokenDgPool {

    public DecoderBrokenPool(){
        super();
    }

    public void joinPoolHandler(Dg data, ChannelId id, List<Dg> array) {
        synchronized (withHead.get(id)) {
            System.out.printf("[%s] 报文异常：%s \n", id.asShortText(), Hex.encodeHexString(data.rebuild()));
            if (Objects.isNull(data.getHead())) {
                // 无头: 查找有头
                IntStream.range(0, withHead.get(id).size()).boxed().forEach((i) -> {
                    Dg head = withHead.get(id).remove();
                    Dg temp = DgTools.checkVaild(DgTools.buildBytes(head.rebuild(), data.rebuild()), id);
                    if (temp.getBroken()) {
                        Optional.of(withHead.get(id).size()).filter(s -> Objects.equals(s,0))
                                .map(s -> withHead.get(id).offer(temp)).orElseGet(() -> withHead.get(id).offer(head));
                    } else {
                        array.add(temp);
                        System.out.printf("[%s] 重新合成: %s\n", id.asShortText(), Hex.encodeHexString(temp.rebuild()));
                    }
                });

            } else {
                // 有头: 入队列
                withHead.get(id).offer(data);
            }
        }
    }

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