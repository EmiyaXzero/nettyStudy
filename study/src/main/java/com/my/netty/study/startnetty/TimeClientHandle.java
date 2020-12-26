package com.my.netty.study.startnetty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: shanghang
 * @Project:nettyStudy
 * @description:TODO
 * @Date: 2020.12.26 15:58
 **/
@Slf4j
public class TimeClientHandle extends ChannelHandlerAdapter {
    private final ByteBuf fisrtMessage;
    TimeClientHandle(){
        byte[] req = "QUERY TIME ORDER".getBytes();
        fisrtMessage = Unpooled.buffer(req.length);
        fisrtMessage.writeBytes(req);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req,"UTF-8");
        log.error("Now is :"+body);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("释放资源");
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //像服务端发消息
        ctx.writeAndFlush(fisrtMessage);
    }
}
