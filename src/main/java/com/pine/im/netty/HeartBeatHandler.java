package com.pine.im.netty;

import com.pine.im.protobuf.MessageProtobuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.stereotype.Component;

/**
 * @Author: lipine
 * @Date: 2020/3/29 15:33
 * @Description: 心跳处理
 * @Version 1.0
 */
@Component
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageProtobuf.Msg  message = (MessageProtobuf.Msg )msg;
        if(message.getDataType() == MessageProtobuf.Msg.DataType.HeartBeatType) {
            MessageProtobuf.HeartBeat heartbeatMsg = message.getHeartbeatMsg();
            String fromId = heartbeatMsg.getFromId();
            ChannelContainer.getInstance().getActiveChannelByUserId(fromId).getChannel().writeAndFlush(message);
        }else{
            ctx.fireChannelRead(msg);
        }
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
