package com.byron.media.server;

import com.byron.media.server.data.DataFactory;
import com.byron.media.server.data.MediaData;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

@ChannelHandler.Sharable
public class MediaDecoder extends ByteToMessageDecoder {

    private DataFactory dataFactory;

    public MediaDecoder(DataFactory dataFactory) {
        this.dataFactory = dataFactory;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int remain = in.readableBytes();
        if(remain < 16){
            return;
        }

        if(remain > 0){
            int originIndex = in.readerIndex();
            in.markReaderIndex(); // 标记读取的位置,方便后面reset

            byte firstByte = in.readByte();

            // RTP 数据
            if(firstByte == '$'){
                int readable = in.readableBytes();

                // RTSP 协议数据
                decodeRtp(originIndex, ctx, in, out);
            } else{

                in.resetReaderIndex();
                decodeString(originIndex, ctx, in, out);
            }
        }
    }


    private boolean decodeRtp(int originIndex, ChannelHandlerContext ctx, ByteBuf in, List<Object> out){
        byte channel = in.readByte();
        byte type = in.readByte();
        byte seq = in.readByte();
        long ID = in.readLong();
        int length = in.readInt();
        int readable = in.readableBytes();

        if(length > readable){
            in.resetReaderIndex();
            return false;
        }

        if(length < 0){
            in.resetReaderIndex();
            return false;
        }

        MediaData media = dataFactory.createData(length);
        in.readBytes(media.getData());
        media.setChannel(channel);
        media.setType(type);
        media.setLength(length);
        media.setID(ID);
        media.setSeq(seq);
        out.add(media);
        return true;
    }

    private boolean decodeString(int originIndex, ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        in.markReaderIndex();

        int read = 0;
        while (in.readableBytes() > 0) {
            byte b = in.readByte();
            read ++;

            if(b == (byte)'\n' || b == (byte)'\r'){

//                int length = in.readerIndex();
                in.resetReaderIndex();
                byte[] data = new byte[read];
                in.readBytes(data);
                String str = new String(data);
                out.add(str);
                return true;
            }
        }

        in.resetReaderIndex();
        return false;
    }
}
