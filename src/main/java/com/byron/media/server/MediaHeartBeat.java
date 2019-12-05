package com.byron.media.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.log4j.Logger;

import java.util.concurrent.TimeUnit;

@ChannelHandler.Sharable
public class MediaHeartBeat extends IdleStateHandler {

    private static Logger logger = Logger.getLogger(MediaHeartBeat.class);

    public MediaHeartBeat(int heartTimeout) {
//        super(0, 0, config.getAllIdleTimeSeconds());
        super(true, 0, 0, heartTimeout, TimeUnit.SECONDS);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
//        Session session = SessionManager.getInstance().findById(ctx.channel().id().asLongText());
        if(evt.state().equals(IdleState.READER_IDLE)){
            logger.info("流媒体读超时");
        }
        if(evt.state().equals(IdleState.WRITER_IDLE)){
            logger.info("流媒体写超时");
        }
        if(evt.state().equals(IdleState.ALL_IDLE)){
            logger.info("流媒体读写超时");
            ctx.close();
        }
    }

}
