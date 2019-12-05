package com.byron.media.server;

import com.byron.media.server.data.MediaData;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

@ChannelHandler.Sharable
public class MediaEncoder extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object message, ChannelPromise promise) throws Exception {
        if(!(message instanceof MediaData)){
            return;
        }
        MediaData data = (MediaData)message;
        ByteBuf buf = ctx.alloc().buffer(data.getLength() + 4);
        buf.writeByte((byte)'$');   // $ 开始符
        buf.writeByte((byte)data.getChannel());     // 通道号
        buf.writeByte((byte)data.getType());     // 类型
        buf.writeByte((byte)data.getSeq());     // 包名
        buf.writeLong(data.getID());           // 单元号
        buf.writeInt(data.getLength());  // 数据长度
        buf.writeBytes(data.getData());       // 数据
        ctx.writeAndFlush(buf, promise);

        data.setFree(true);

    }
}
