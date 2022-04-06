package org.lojoso.sudie.mesh.common.model;

import io.netty.channel.ChannelId;
import org.apache.commons.codec.binary.Hex;

import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.Optional;

public class Dg {

    private ChannelId id;
    private Boolean broken;
    private int total;

    public Dg() {
    }

    public Dg(ChannelId id, byte[] body) {
        this.id = id;
        this.body = body;
        this.broken = true;
        this.total = body.length;
    }

    public Dg(ChannelId id, Boolean broken, byte[] head, byte[] afn, byte[] length, byte[] crc, byte[] body) {
        this.id = id;
        this.broken = broken;
        this.head = head;
        this.afn = afn;
        this.length = length;
        this.crc = crc;
        this.body = body;
        this.total = head.length + afn.length + length.length + crc.length + body.length;
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
        this.total = this.total + head.length;
        this.head = head;
    }

    public byte[] getAfn() {
        return afn;
    }

    public void setAfn(byte[] afn) {
        this.total = this.total + afn.length;
        this.afn = afn;
    }

    public byte[] getLength() {
        return length;
    }

    public void setLength(byte[] length) {
        this.total = this.total + length.length;
        this.length = length;
    }

    public byte[] getCrc() {
        return crc;
    }

    public void setCrc(byte[] crc) {
        this.total = this.total + crc.length;
        this.crc = crc;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.total = this.total + body.length;
        this.body = body;
    }

    public byte[] combine(byte[] change) {

        ByteBuffer buffer =
                ByteBuffer.allocate(total);
        Optional.ofNullable(head).ifPresent(buffer::put);
        Optional.ofNullable(afn).ifPresent(a -> buffer.put(Objects.isNull(change) ? a : change));
        Optional.ofNullable(length).ifPresent(buffer::put);
        Optional.ofNullable(crc).ifPresent(buffer::put);
        Optional.ofNullable(body).ifPresent(buffer::put);
        return buffer.array();
    }

    public byte[] rebuild() {
        return this.combine(null);
    }

    @Override
    public String toString() {
        return Optional.ofNullable(body).map(Hex::encodeHexString).orElse("EMPTY");
    }
}
