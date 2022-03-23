package org.lojoso.sudie.mesh.center.config;

public class SudieBaseConfig {

    // server端口
    private Integer port;
    // 服务集群urls
    private String clusters;
    // 重试次数
    private Integer retry;

    public SudieBaseConfig(){

    }

    public SudieBaseConfig(String port){
        this.port = Integer.parseInt(port);
    }

    public SudieBaseConfig(String port, String clusters){
        this.port = Integer.parseInt(port);
        this.clusters = clusters;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getClusters() {
        return clusters;
    }

    public void setClusters(String clusters) {
        this.clusters = clusters;
    }

    public Integer getRetry() {
        return retry;
    }

    public void setRetry(Integer retry) {
        this.retry = retry;
    }
}
