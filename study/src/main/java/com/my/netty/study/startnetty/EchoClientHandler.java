package com.my.netty.study.startnetty;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shanghang
 * @title: EchoClientHandler
 * @projectName nettyStudy
 * @description: 分隔符解码器
 * @date 2020.12.27-15:10
 */
@Slf4j
public class EchoClientHandler extends ChannelHandlerAdapter {
    private int counter;
    static final String ECHO_REQ = "this is a req $_" ;

    EchoClientHandler(){

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0 ;i< 10;i++){
            ctx.writeAndFlush(Unpooled.copiedBuffer(ECHO_REQ.getBytes()));
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.error("this is "+ ++counter +"receiver "+msg);
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
