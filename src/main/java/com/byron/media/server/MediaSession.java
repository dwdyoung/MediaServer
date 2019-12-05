package com.byron.media.server;

import com.byron.media.server.data.MediaData;
import com.byron.media.server.data.MediaDescriptionData;
import com.byron.media.server.data.RegisterMessage;
import com.google.gson.Gson;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

//@ChannelHandler.Sharable
public class MediaSession extends ChannelInboundHandlerAdapter {

    private boolean active;     // 是否注册

    private boolean sender;        // 是否接收 false:接收端  true:发送端     一个通道只能有一个发送端

    private String clientName;      // 客户端名字

    private String groupName;       // 媒体组名字

    private ChannelHandlerContext context;

    private Gson gson = new Gson();

    private MediaGroup mediaGroup;      // 终端所在媒体组

    private MediaCenter mediaCenter = MediaCenter.getInstance();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if(context == null){
            context = ctx;
        }

        if(msg instanceof String){
//            groupName = (String) msg;
            RegisterMessage registerMessage = gson.fromJson((String)msg, RegisterMessage.class);
            this.sender = registerMessage.isSender();
            this.clientName = registerMessage.getClientName();
            this.groupName = registerMessage.getGroupName();

            // 注册流程
            if(registerMessage.isSender()){
                mediaCenter.registerSender(registerMessage.getGroupName(), this, registerMessage.getMediaDescription());
                // 返回ok
            } else {
                MediaDescriptionData mediaDescription = mediaCenter.registerReceiver(registerMessage.getGroupName(), this);
            }

            ctx.fireChannelActive();
            ctx.fireChannelReadComplete();
            active = true;

        } else if(active){
            ctx.fireChannelRead(msg);
        }
    }

    public String getGroupName() {
        return groupName;
    }

    /**
     * 输出数据
     */
    public void write(MediaData data) {
        if(context == null){
            return;
        }
        context.writeAndFlush(data);
    }

    public void setMediaGroup(MediaGroup mediaGroup) {
        this.mediaGroup = mediaGroup;
    }

    public MediaGroup getMediaGroup() {
        return mediaGroup;
    }
}
