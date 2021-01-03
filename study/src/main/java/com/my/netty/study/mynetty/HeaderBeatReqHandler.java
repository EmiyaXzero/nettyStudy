package com.my.netty.study.mynetty;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @Author: shanghang
 * @Project:nettyStudy
 * @description:客户端心跳检测处理
 * @Date: 2021.01.03 23:31
 **/
@Slf4j
public class HeaderBeatReqHandler extends ChannelHandlerAdapter {
    private volatile ScheduledFuture<?> heartBeat;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;

        //握手成功，主动发送心跳消息
        if(message.getHeader() != null
         && message.getHeader().getType() == MessageType.LOGIN_RESP.getValue()){
            //定时发送监听
            heartBeat = ctx.executor().scheduleAtFixedRate(
                    new HeaderBeatReqHandler.HeartBeatTask(ctx),0,5000, TimeUnit.MILLISECONDS);
        }else if(message.getHeader() != null
        && message.getHeader().getType() == MessageType.HEARTBEAT_RESP.getValue()){
            log.error("Client receive server heart beat message" + message.toString());
        }else {
            ctx.fireChannelRead(msg);
        }
    }

    private class HeartBeatTask implements Runnable{
        private final ChannelHandlerContext ctx;

        public HeartBeatTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            NettyMessage heatBeat = buildHeatBeat();
            log.error("heat beat send :"+heatBeat.toString());
            ctx.writeAndFlush(heatBeat);
        }

        private NettyMessage buildHeatBeat() {
            NettyMessage message = new NettyMessage();
            Header header = new Header();
            header.setType(MessageType.HEARTBEAT_REQ.getValue());
            message.setHeader(header);
            return message;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if(heartBeat != null){
            heartBeat.cancel(true);
            heartBeat = null;
        }
        ctx.fireExceptionCaught(cause);
    }
}
