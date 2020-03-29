package com.pine.im.netty;

import com.pine.im.protobuf.MessageProtobuf;
import com.pine.im.service.IMUserService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: lipine
 * @Date: 2020/3/26 18:02
 * @Description: 登录授权处理器
 * @Version 1.0
 */
@Component
public class LoginAuthoHandler extends ChannelInboundHandlerAdapter {

    private static final String TAG = "LoginAuthoHandler";

    private String token = "test";

    @Autowired
    private  IMUserService imUserService;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageProtobuf.Msg  message = (MessageProtobuf.Msg )msg;
        if(message.getDataType() == MessageProtobuf.Msg.DataType.LoginRequestMsgType){
            MessageProtobuf.LoginRequestMsg loginRequestMsg = message.getLoginRequestMsg();
            System.out.println(TAG+"=========="+loginRequestMsg);
            String uid = imUserService.getLoginResponse(loginRequestMsg.getUsername(),loginRequestMsg.getPassword());
            if(uid!=null){
                // 握手成功后，保存用户通道
                ChannelContainer.getInstance().saveChannel(new NettyChannel(uid, ctx.channel()));
                MessageProtobuf.Msg responMessage = MessageProcessor.getLoginSuccessResponse(uid,token,1);
                ChannelContainer.getInstance().getActiveChannelByUserId(uid).getChannel().writeAndFlush(responMessage);
            }else{
                MessageProtobuf.Msg responMessage = MessageProcessor.getLoginFailedResponse(0);
                ChannelContainer.getInstance().getActiveChannelByUserId(uid).getChannel().writeAndFlush(responMessage);
                //取消保存通道
//              ChannelContainer.getInstance().removeChannelIfConnectNoActive(ctx.channel());
            }
        }else{
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
