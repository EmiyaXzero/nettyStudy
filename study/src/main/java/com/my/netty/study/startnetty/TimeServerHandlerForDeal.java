package com.my.netty.study.startnetty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * @Author: shanghang
 * @Project:nettyStudy
 * @description:netty服务端处理适配器-解决TCP粘包
 * @Date: 2020.12.26 15:36
 **/
@Slf4j
public class TimeServerHandlerForDeal extends ChannelHandlerAdapter {
    private int counter;
    TimeServerHandlerForDeal(){

    }

     @Override
     public void channelRead(ChannelHandlerContext ctx , Object msg) throws UnsupportedEncodingException {
        //直接取传进来的msg,不需要处理
        String body = (String) msg;
        log.error("The time server recive order: "+ body+"; the counter is "+ ++ counter);
         String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)?new Date(System.currentTimeMillis()).toString():"bad req";
         currentTime += System.getProperty("line.separator");
         ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
         ctx.writeAndFlush(resp);
     }

     @Override
     public void channelReadComplete(ChannelHandlerContext ctx){
        ctx.flush();
     }
     @Override
     public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        ctx.close();
     }
}
