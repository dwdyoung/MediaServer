package com.byron.media.server.data;

public class MediaData {

    private byte channel;       // 通道号

    private byte type;          // 类型

    private byte seq;          // 包名

    private long ID;            // 单元号

    private byte[] data;        // 数据长度

    private int length;         // 数据长度

    private boolean free = true;           // 是否空闲 （用于循环利用）

    public MediaData(byte channel, byte type, long ID, byte[] data, int length) {
        this.channel = channel;
        this.type = type;
        this.ID = ID;
        this.data = data;
        this.length = length;
    }

    public byte getChannel() {
        return channel;
    }

    public void setChannel(byte channel) {
        this.channel = channel;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte getSeq() {
        return seq;
    }

    public void setSeq(byte seq) {
        this.seq = seq;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }
}
