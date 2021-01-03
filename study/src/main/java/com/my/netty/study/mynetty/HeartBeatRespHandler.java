package com.my.netty.study.mynetty;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: shanghang
 * @Project:nettyStudy
 * @description:心跳服务端
 * @Date: 2021.01.03 23:41
 **/
@Slf4j
public class HeartBeatRespHandler extends ChannelHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;

        if(message.getHeader() != null
            && message.getHeader().getType() == MessageType.HEARTBEAT_REQ.getValue()){
            //处理心跳请求
            NettyMessage heartBeat = buildHeatBeat();
            log.error("server send heartBeat response :"+heartBeat.toString());
            ctx.writeAndFlush(heartBeat);
        }else{
            //透传
            ctx.fireChannelRead(msg);
        }
    }

    private NettyMessage buildHeatBeat() {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.HEARTBEAT_RESP.getValue());
        message.setHeader(header);
        return message;
    }
}
