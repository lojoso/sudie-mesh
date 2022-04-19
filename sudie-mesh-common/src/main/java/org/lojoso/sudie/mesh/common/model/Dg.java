package org.lojoso.sudie.mesh.common.model;

import io.netty.channel.ChannelId;
import org.apache.commons.codec.binary.Hex;

import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.Optional;

public class Dg {

    private ChannelId id;
    private int total;

    public Dg() {
    }

    public Dg(ChannelId id, byte[] body) {
        this.id = id;
        this.body = body;
        this.total = body.length;
    }

    public Dg(ChannelId id, byte[] afn, byte[] length, byte[] body) {
        this.id = id;
        this.afn = afn;
        this.length = length;
        this.body = body;
        this.total = afn.length + length.length + body.length;
    }

    // 报文afn: 1
    private byte[] afn;
    // 报文数据长度: 2
    private byte[] length;
    // 报文数据: n
    private byte[] body;

    public ChannelId getId() {
        return id;
    }

    public void setId(ChannelId id) {
        this.id = id;
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

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.total = this.total + body.length;
        this.body = body;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public byte[] combine(byte[] change) {

        ByteBuffer buffer =
                ByteBuffer.allocate(total);
        Optional.ofNullable(afn).ifPresent(a -> buffer.put(Objects.isNull(change) ? a : change));
        Optional.ofNullable(length).ifPresent(buffer::put);
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
