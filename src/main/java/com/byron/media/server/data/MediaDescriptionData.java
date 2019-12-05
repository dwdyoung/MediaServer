package com.byron.media.server.data;

import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.List;

/**
 * 媒体描述
 */
public class MediaDescriptionData {


    private List<ChannelDescription> channelDescriptions = new ArrayList<>();           // 发送端有效

    public List<ChannelDescription> getChannelDescriptions() {
        return channelDescriptions;
    }

    public void setChannelDescriptions(List<ChannelDescription> channelDescriptions) {
        this.channelDescriptions = channelDescriptions;
    }

    /**
     * 通道描述
     */
    public static class ChannelDescription {

        private byte channel;               // 通道号
        private int mediaType;              // 媒体类型
        private int width;                  // 高宽（视频时有效）
        private int height;                 // 高宽（视频时有效）
        private int size;                   // 视频大小

        public byte getChannel() {
            return channel;
        }

        public void setChannel(byte channel) {
            this.channel = channel;
        }

        public int getMediaType() {
            return mediaType;
        }

        public void setMediaType(int mediaType) {
            this.mediaType = mediaType;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }
    }
}
