package org.lojoso.sudie.mesh.center.kernel.data;

import io.netty.channel.ChannelId;
import org.lojoso.sudie.mesh.center.kernel.model.Dg;
import org.lojoso.sudie.mesh.center.utils.DgTools;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

// 断帧池
public class BrokenDgPool {

    private final ConcurrentHashMap<ChannelId, ArrayDeque<Dg>> withHead;

    public BrokenDgPool(){
        this.withHead = new ConcurrentHashMap<>();
    }

    private void joinPoolHandler(Dg data, ChannelId id, List<Dg> array){
        synchronized (withHead.get(id)){
            if(Objects.isNull(data.getHead())){
                // 无头: 查找有头
                Dg head = withHead.get(id).remove();
                Dg temp = DgTools.checkVaild(DgTools.buildBytes(head.rebuild(), data.rebuild()), id);
                if(temp.getBroken()){
                    withHead.get(id).offer(temp);
                }else {
                    array.add(temp);
                };
            }else {
                // 有头: 入队列
                withHead.get(id).offer(data);
            }
        }
    }

    private void initQueue(ChannelId id){
        this.withHead.putIfAbsent(id, new ArrayDeque<>());
    }

    public List<Dg> combineDg(List<Dg> datas, ChannelId id){
        initQueue(id);
        List<Dg> result = new ArrayList<>();
        datas.stream().filter(Dg::getBroken).forEach(d -> this.joinPoolHandler(d, id, result));
        return result;
    }

}
