package com.byron.media.server.data;

/**
 * 终端连接上服务器需要发送的注册信息
 */
public class RegisterMessage {

    private boolean sender;        // 是否接收 false:接收端  true:发送端     一个通道只能有一个发送端

    private String clientName;      // 客户端名字

    private String groupName;       // 媒体组名字

    private MediaDescriptionData mediaDescription;

    public MediaDescriptionData getMediaDescription() {
        return mediaDescription;
    }

    public void setMediaDescription(MediaDescriptionData mediaDescription) {
        this.mediaDescription = mediaDescription;
    }

    public boolean isSender() {
        return sender;
    }

    public void setSender(boolean sender) {
        this.sender = sender;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
