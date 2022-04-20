package org.lojoso.sudie.mesh.common.config;

public interface DefaultConfig {

    // SocketClient 配置
    int CLIENT_HB_SECONDS = 30;

    // SocketServer 配置
    int SERVER_HB_SECONDS = 60;

    // LengthFieldBasedFrameDecoder 配置
    int LENBASED_DE_LEN = 2;
    int LENBASED_DE_OFFSET = 1;
    int LENBASED_DE_LEN_ADJUST = 0;
    int LENBASED_DE_MAX_LEN = 10240;

    // ConsumerTimeOut 配置
    int CONSUMER_TIMEOUT_SECONDS = 5;

    int CLIENT_N_THREADS = 2;

}
