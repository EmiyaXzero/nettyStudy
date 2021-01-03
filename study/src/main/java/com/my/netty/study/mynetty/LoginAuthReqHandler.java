package com.my.netty.study.mynetty;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: shanghang
 * @Project:nettyStudy
 * @description:握手客户端
 * @Date: 2021.01.03 23:07
 **/
@Slf4j
public class LoginAuthReqHandler extends ChannelHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(buildLoginReq());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;
        //如果是握手应答消息，需要判断是否认证成功
        if(message.getHeader() != null &&
            message.getHeader().getType() == MessageType.LOGIN_RESP.getValue()){
            byte loginResult = (byte) message.getBody();
            if(loginResult != 0){
                ctx.close();
            }else{
                log.error("Login is ok :"+message);
                ctx.fireChannelRead(msg);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.fireExceptionCaught(cause);
    }

    private NettyMessage buildLoginReq() {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.LOGIN_REQ.getValue());
        message.setHeader(header);
        message.setBody("123");
        return message;
    }

}
