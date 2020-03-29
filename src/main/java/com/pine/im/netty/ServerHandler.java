package com.pine.im.netty;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pine.im.protobuf.MessageProtobuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

class ServerHandler extends ChannelInboundHandlerAdapter {

    private static final String TAG = ServerHandler.class.getSimpleName();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        System.out.println("ServerHandler channelActive()" + ctx.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.out.println("ServerHandler channelInactive()");
        // 用户断开连接后，移除channel
        ChannelContainer.getInstance().removeChannelIfConnectNoActive(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        System.out.println("ServerHandler exceptionCaught()");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        System.out.println("ServerHandler userEventTriggered()");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageProtobuf.Msg message = (MessageProtobuf.Msg) msg;
        System.out.println("收到来自客户端的消息：" + message);
        int msgType = message.getDataTypeValue();
//        int msgType = message.getHead().getMsgType();
        switch (msgType) {
            // 握手消息
            case MessageProtobuf.Msg.DataType.LoginResponseMsgType_VALUE: {
                String fromId = message.getCommonMsg().getFromId();
                JSONObject jsonObj = JSON.parseObject(message.getCommonMsg().getExtend());
                String userName = jsonObj.getString("userName");
                JSONObject resp = new JSONObject();
                if (userName.equals(fromId)) {
                    resp.put("status", 1);
                    // 握手成功后，保存用户通道
                    ChannelContainer.getInstance().saveChannel(new NettyChannel(fromId, ctx.channel()));
                } else {
                    resp.put("status", -1);
                    ChannelContainer.getInstance().removeChannelIfConnectNoActive(ctx.channel());
                }

                message = message.toBuilder().setCommonMsg(message.getCommonMsg().toBuilder().setExtend(resp.toString()).build()).build();
                ChannelContainer.getInstance().getActiveChannelByUserId(fromId).getChannel().writeAndFlush(message);
                break;
            }

            // 心跳消息
            case 1002: {
                // 收到心跳消息，原样返回
                String fromId = message.getCommonMsg().getFromId();
                ChannelContainer.getInstance().getActiveChannelByUserId(fromId).getChannel().writeAndFlush(message);
                break;
            }

            case 2001: {
                // 收到2001或3001消息，返回给客户端消息发送状态报告
//                String fromId = message.getCommonMsg().getFromId();
//                MessageProtobuf.Msg.Builder sentReportMsgBuilder = MessageProtobuf.Msg.newBuilder();
//                MessageProtobuf.Head.Builder sentReportHeadBuilder = MessageProtobuf.Head.newBuilder();
//                sentReportHeadBuilder.setMsgId(message.getCommonMsg().getMsgId());
//                sentReportHeadBuilder.setMsgType(1010);
//                sentReportHeadBuilder.setTimestamp(System.currentTimeMillis());
//                sentReportHeadBuilder.setStatusReport(1);
//                sentReportMsgBuilder.setHead(sentReportHeadBuilder.build());
//                ChannelContainer.getInstance().getActiveChannelByUserId(fromId).getChannel().writeAndFlush(sentReportMsgBuilder.build());
//
//                // 同时转发消息到接收方
//                String toId = message.getCommonMsg().getToId();
//                ChannelContainer.getInstance().getActiveChannelByUserId(toId).getChannel().writeAndFlush(message);
                break;
            }
            case 3001: {
                // todo 群聊，自己实现吧，toId可以是群id，根据群id查找所有在线用户的id，循环遍历channel发送即可。
                break;
            }

            default:
                break;
        }
    }


}
