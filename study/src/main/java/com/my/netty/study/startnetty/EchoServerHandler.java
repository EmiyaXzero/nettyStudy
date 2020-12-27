package com.my.netty.study.startnetty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shanghang
 * @title: EchoServerHandler
 * @projectName nettyStudy
 * @description: 分隔符解码器
 * @date 2020.12.27-14:52
 */
@Slf4j
public class EchoServerHandler extends ChannelHandlerAdapter {
    int counter = 0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body = (String) msg;
        log.error("this is " + ++counter + "次:"+ body);
        //返回客户端也需要增加分隔符
        body +="$_";
        ByteBuf echo = Unpooled.copiedBuffer(body.getBytes());
        ctx.writeAndFlush(echo);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
