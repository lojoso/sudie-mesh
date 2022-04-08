package org.lojoso.sudie.mesh.common.model;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public interface CommonData {

    // 报文头
    byte[] HEAD = { 0x68 };
    // 心跳AFN
    byte[] HB_AFN = { 0x00 };
    // 心跳长度
    byte[] HB_LEN = { 0x00, 0x08 };

    /**
     * From客户端 - 消息产生(服务调用)
     *
     * body ->
     * Seq：random(.4[float])
     * ClassIndex: 映射Class序号
     * Method： Method序号
     * V[n]Len: 参数n的长度
     * V: 参数序列化
     */
    byte[] CD_AFN = { 0x01 };

    /**
     * From客户端 - 服务注册
     *
     * body ->
     * className: 当前注册className
     * version: class版本
     */
    byte[] CD_AFN_REG = { 0X02 };
    // 服务端 均衡拉取
    byte[] SD_AFN_PULL = { 0x11 };
    // 服务端 均衡入队列
    byte[] SD_AFN_PUSH = { 0x12 };

}
