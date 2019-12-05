package com.byron.media.server;

import com.byron.media.server.data.DataFactory;
import com.byron.media.server.data.MediaData;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.apache.log4j.Logger;

public class MediaServer extends Thread {

    private static final Logger logger = Logger.getLogger(MediaServer.class);

    private ServerBootstrap bootstrap;

    private ChannelFuture closeFuture;

    private DataFactory dataFactory = new DataFactory();

    private int port;       // 流媒体服务器监听端口

    private int heartTimeout;       // 流媒体超时时间

    public MediaServer(int port, int heartTimeout) {
        this.port = port;
        this.heartTimeout = heartTimeout;
    }

    @Override
    public void run() {

        EventLoopGroup bossGroup = new NioEventLoopGroup(10);
        EventLoopGroup workerGroup = new NioEventLoopGroup(10);
        try {
            bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .option(ChannelOption.SO_RCVBUF, 300 * 1024 * 1024)
                    .option(ChannelOption.SO_SNDBUF, 300 * 1024 * 1024)
                    .option(ChannelOption.MAX_MESSAGES_PER_READ, 100)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new ChunkedWriteHandler());
                            p.addLast(new MediaDecoder(dataFactory));
                            p.addLast(new MediaEncoder());
                            if(heartTimeout > 0){
                                p.addLast(new MediaHeartBeat(heartTimeout));
                            }
                            p.addLast(new MediaSession());
                            p.addLast(new MediaHandler());
                        }
                    });
//                    .childOption(ChannelOption.AUTO_READ, true);

            // Start the server.
            closeFuture = bootstrap.bind(port).sync();

            logger.info("Gonsin Media Server 配置启动完成");

            // Wait until the server socket is closed.
            closeFuture.channel().closeFuture().sync();
            logger.info("Gonsin Media Server 关闭");

//            System.out.println("EchoServer.main end");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public void end(){
        closeFuture.channel().pipeline().fireChannelInactive();
    }

//    /**
//     * 发送到组
//     */
//    public void sendToGroup(MediaData media){
////        MediaData mediaData = dataFactory.createData(channel, type, ID, data, length);
//        sessionContainer.sendToGroup(group, mediaData);
//    }

    public int getPort() {
        return port;
    }

    public int getHeartTimeout() {
        return heartTimeout;
    }
}
