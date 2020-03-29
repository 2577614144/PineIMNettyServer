package com.pine.im.netty;

import com.pine.im.protobuf.MessageProtobuf;

/**
 * @Author: lipine
 * @Date: 2020/3/27 14:48
 * @Description: 消息封装处理
 * @Version 1.0
 */
public class MessageProcessor {

    /**
     * 构建登录成功响应
     * @return
     */
    public static MessageProtobuf.Msg getLoginSuccessResponse(String userId,String token,int status) {
        MessageProtobuf.Msg msg = MessageProtobuf.Msg.newBuilder()
                .setDataType(MessageProtobuf.Msg.DataType.LoginResponseMsgType)
                .setLoginResponseMsg(MessageProtobuf.LogResponseMsg.newBuilder()
                        .setUserId(userId)
                        .setToken(token)
                        .setStatus(status)
                        .build())
                .build();
        return msg;
    }
    /**
     * 构建登录失败响应
     * @return
     */
    public static MessageProtobuf.Msg getLoginFailedResponse(int status) {
        MessageProtobuf.Msg msg = MessageProtobuf.Msg.newBuilder()
                .setDataType(MessageProtobuf.Msg.DataType.LoginResponseMsgType)
                .setLoginResponseMsg(MessageProtobuf.LogResponseMsg.newBuilder()
                        .setStatus(status)
                        .build())
                .build();
        return msg;
    }
}
