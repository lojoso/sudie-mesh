package org.lojoso.sudie.mesh.common.config;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public interface CommonData {

    // 心跳AFN
    byte[] HB_AFN = { 0x00 };

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

    /**
     * From客户端 - 调用返回
     * body -> state
     *         state + result_length + resBody
     *         state + error_length + error
     */
    byte[] CD_AFN_RES = { 0X03 };

    /**
     * From客户端 - consumer注册
     *
     * body ->
     * uuid
     */
    byte[] CD_AFN_CLI_REG = { 0X04 };

    // 服务端 均衡拉取
    byte[] SD_AFN_PULL = { 0x11 };
    // 服务端 均衡入队列
    byte[] SD_AFN_PUSH = { 0x12 };

}
