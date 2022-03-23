package org.lojoso.sudie.mesh.center.kernel.model;

import io.netty.channel.ChannelId;
import org.apache.commons.codec.binary.Hex;

public class Dg {

    private ChannelId id;
    private Boolean broken;

    public Dg() {
    }

    public Dg(ChannelId id, byte[] body){
        this.id = id;
        this.body = body;
        this.broken = true;
    }

    public Dg(ChannelId id, Boolean broken, byte[] head, byte[] afn, byte[] length, byte[] crc, byte[] body) {
        this.id = id;
        this.broken = broken;
        this.head = head;
        this.afn = afn;
        this.length = length;
        this.crc = crc;
        this.body = body;
    }

    // 报文头：1
    private byte[] head;
    // 报文afn: 1
    private byte[] afn;
    // 报文数据长度: 2
    private byte[] length;
    // 报文crc校验码: 1
    private byte[] crc;
    // 报文数据: n
    private byte[] body;

    public ChannelId getId() {
        return id;
    }

    public void setId(ChannelId id) {
        this.id = id;
    }

    public Boolean getBroken() {
        return broken;
    }

    public void setBroken(Boolean broken) {
        this.broken = broken;
    }

    public byte[] getHead() {
        return head;
    }

    public void setHead(byte[] head) {
        this.head = head;
    }

    public byte[] getAfn() {
        return afn;
    }

    public void setAfn(byte[] afn) {
        this.afn = afn;
    }

    public byte[] getLength() {
        return length;
    }

    public void setLength(byte[] length) {
        this.length = length;
    }

    public byte[] getCrc() {
        return crc;
    }

    public void setCrc(byte[] crc) {
        this.crc = crc;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return Hex.encodeHexString(body);
    }
}
