package com.my.netty.study.frame.websocket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: shanghang
 * @Project:nettyStudy
 * @description:TODO
 * @Date: 2021.01.01 23:38
 **/
@Slf4j
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {
    private WebSocketServerHandshaker handshaker;


    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
        //传统的http接入
        if(msg instanceof FullHttpRequest){
            handlerHttpRequest(ctx,(FullHttpRequest)msg);
        }else if(msg instanceof WebSocketFrame){
            handlerWebSocketRequest(ctx,(WebSocketFrame) msg);
        }
    }



    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    private void handlerHttpRequest(ChannelHandlerContext ctx, FullHttpRequest request) {
        if(!request.getDecoderResult().isSuccess()
            || !"websocket".equals(request.headers().get("Upgrade"))){
            sendHttpResponse(ctx,request,new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }

        //构造握手响应放回，本机测试
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory("ws://localhoost:8080/websocket",null,false);
        handshaker = wsFactory.newHandshaker(request);
        if(handshaker == null){
            WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());
        }else{
            handshaker.handshake(ctx.channel(),request);
        }
    }



    private void handlerWebSocketRequest(ChannelHandlerContext ctx, WebSocketFrame request) {
        if(request instanceof CloseWebSocketFrame){
            handshaker.close(ctx.channel(),((CloseWebSocketFrame) request).retain());
            return;
        }

        if(request instanceof PingWebSocketFrame){
            //判断是否是ping消息
            ctx.channel().write(new PongWebSocketFrame(request.content().retain()));
            return;
        }

        String requestString = ((TextWebSocketFrame) request).text();
        log.error("%s received %s",ctx.channel(),requestString);
        ctx.channel().write(new TextWebSocketFrame(requestString +
                "欢迎使用Netty webSocket服务"));
    }

    private void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse defaultHttpResponse) {
        if (defaultHttpResponse.getStatus().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(
                    defaultHttpResponse.getStatus().toString(),
                    CharsetUtil.UTF_8
            );
            defaultHttpResponse.content().writeBytes(buf);
            buf.release();
            HttpHeaders.setContentLength(defaultHttpResponse, defaultHttpResponse.content().readableBytes());
        }
        ChannelFuture f = ctx.channel().writeAndFlush(defaultHttpResponse);
        if(!HttpHeaders.isKeepAlive(request) || defaultHttpResponse.getStatus().code() != 200){
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();;
        ctx.close();
    }
}
