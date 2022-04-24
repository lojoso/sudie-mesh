package org.lojoso.sudie.mesh.center.config;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;

public class SslContextConfig {

    private SslContext serverContext;
    private SslContext clientContext;

    public SslContextConfig(){
//        clientContext = SslContextBuilder.forClient().keyManager().trustManager().build();

    }

    public SslContext getServerContext() {
        return serverContext;
    }

    public void setServerContext(SslContext serverContext) {
        this.serverContext = serverContext;
    }

    public SslContext getClientContext() {
        return clientContext;
    }

    public void setClientContext(SslContext clientContext) {
        this.clientContext = clientContext;
    }
}
