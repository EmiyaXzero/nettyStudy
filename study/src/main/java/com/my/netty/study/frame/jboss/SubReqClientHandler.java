package com.my.netty.study.frame.jboss;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: shanghang
 * @Project:nettyStudy
 * @description:TODO
 * @Date: 2020.12.28 22:27
 **/
@Slf4j
public class SubReqClientHandler extends ChannelHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i <10 ; i++) {
            ctx.write(createReq(i));
        }
        ctx.flush();
    }

    private Request createReq(int i) {
        Request request = new Request();
        request.setAge(i);
        request.setName("aaa");
        return request;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.error(msg.toString());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
