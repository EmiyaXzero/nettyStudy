package com.my.netty.study.mynetty;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: shanghang
 * @Project:nettyStudy
 * @description:TODO
 * @Date: 2021.01.03 23:18
 **/
@Slf4j
public class LoginAuthRespHandler extends ChannelHandlerAdapter {
    private Map<String,Boolean> nodeCheck = new ConcurrentHashMap<>();

    private Set<String> whiteList = new HashSet<String>(){
        {add("127.0.0.1");}
    };

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;
        log.error("The woshou response is " + message.toString());

        //如果是握手消息处理，其他消息透传
        if(msg != null && message.getHeader() != null &&
           message.getHeader().getType() == MessageType.LOGIN_REQ.getValue()){
            //客户端节点标识
            String nodeIndex = ctx.channel().remoteAddress().toString();
            NettyMessage loginResp = null;
            //重复登陆，拒绝
            if(nodeCheck.containsKey(nodeIndex)){
                loginResp = buildResponse((byte)-1);
            }else {
                InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
                String ip = address.getAddress().getHostAddress();
                if(whiteList.contains(ip)){
                    loginResp = buildResponse((byte)0);
                    nodeCheck.put(nodeIndex,true);
                }else {
                    loginResp = buildResponse((byte)-1);
                }
            }
            log.error("The login response is " + loginResp.toString());
            ctx.writeAndFlush(loginResp);
        }else {
            //请求管道透传
            ctx.fireChannelRead(msg);
        }
    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        nodeCheck.remove(ctx.channel().remoteAddress().toString());
        ctx.close();
        ctx.fireExceptionCaught(cause);
    }

    private NettyMessage buildResponse(byte b) {
        NettyMessage response = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.LOGIN_RESP.getValue());
        response.setHeader(header);
        response.setBody(b);
        return response;
    }
}
