package com.pine.im.netty;

import com.pine.im.protobuf.MessageProtobuf;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>@ProjectName:     BoChat</p>
 * <p>@ClassName:       NettyServerDemo.java</p>
 * <p>@PackageName:     com.bochat.im.netty</p>
 * <b>
 * <p>@Description:     TCP netty服务端</p>
 * </b>
 * <p>@author:          FreddyChen</p>
 * <p>@date:            2019/02/15 14:42</p>
 * <p>@email:           chenshichao@outlook.com</p>
 */
@Slf4j
@Component
public class TCPNettyServer {

    private  EventLoopGroup bossGroup;
    private  EventLoopGroup workerGroup;
    private  Channel channel;

    @Autowired
    private LoginAuthoHandler loginAuthoHandler;

    public ChannelFuture startServer(String hostname, int port) throws Exception {
        //boss线程监听端口，worker线程负责数据读写
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        ChannelFuture future = null;
        try {
            //辅助启动类
            ServerBootstrap bootstrap = new ServerBootstrap();
            //设置线程池
            bootstrap.group(bossGroup, workerGroup);
            //设置socket工厂
            bootstrap.channel(NioServerSocketChannel.class);
            //设置管道工厂
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    //获取管道
                    ChannelPipeline pipeline = socketChannel.pipeline();
//                    pipeline.addLast("frameEncoder", new LengthFieldPrepender(2));
//                    pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(65535,
//                            0, 2, 0, 2));

                    //Netty自带Protobuf编码、解码器处理半包、拆包/粘包
                    pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
                    pipeline.addLast(new ProtobufVarint32FrameDecoder());

                    pipeline.addLast(new ProtobufDecoder(MessageProtobuf.Msg.getDefaultInstance()));
                    pipeline.addLast(new ProtobufEncoder());
//                    pipeline.addLast(new LoginAuthoHandler());
                    pipeline.addLast(loginAuthoHandler);
                    //处理类
                    pipeline.addLast(new ServerHandler());
                }
            });

            //设置TCP参数
            //1.链接缓冲池的大小（ServerSocketChannel的设置）
            bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
            //维持链接的活跃，清除死链接(SocketChannel的设置)
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            //关闭延迟发送
            bootstrap.childOption(ChannelOption.TCP_NODELAY, true);

            //绑定端口
            future = bootstrap.bind(port).sync();
            System.out.println("server start ...... ");

            //等待服务端监听端口关闭
            future.channel().closeFuture().sync();
            channel = future.channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (future!= null && future.isSuccess()) {
                log.info("Netty server listening " + hostname + " on port " + port + " and ready for connections...");
            } else {
                log.error("Netty server start up Error!");
            }
        }
        return future;
    }

    /**
     * 关闭服务器
     */
    public void destroyServer(){
        if(channel != null) {
            channel.close();
        }
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }
}


