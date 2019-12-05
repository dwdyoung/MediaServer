package com.byron.media.server;

import com.byron.media.server.data.MediaData;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@ChannelHandler.Sharable
public class MediaHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof MediaData){
            MediaSession session = ctx.pipeline().get(MediaSession.class);
            session.getMediaGroup().sendToAll((MediaData)msg);
        }
        ctx.fireChannelReadComplete();
    }
}
