package com.my.netty.study.startnetty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: shanghang
 * @Project:nettyStudy
 * @description:netty客户端处理适配器模拟粘包
 * @Date: 2020.12.26 15:58
 **/
@Slf4j
public class TimeClientHandleForHalf extends ChannelHandlerAdapter {

    private byte[] req;

    private int counter;

    TimeClientHandleForHalf(){
        req = ("QUERY TIME ORDER"+ System.getProperty("line.separator")).getBytes();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req,"UTF-8");
        log.error("Now is :"+body+"; the counter is :"+ ++counter);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("释放资源");
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf message = null;
        for (int i = 0; i < 100; i++) {
            message = Unpooled.buffer(req.length);;
            message.writeBytes(req);
            //向服务端发消息
            ctx.writeAndFlush(message);
        }
    }
}
