package com.my.netty.study.startnetty.msgpack;

import com.my.netty.study.io.nettey.handler.codec.msgpack.MsgPackDecoder;
import com.my.netty.study.io.nettey.handler.codec.msgpack.MsgPackEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

/**
 * @Author: shanghang
 * @Project:nettyStudy
 * @description:netty客户端-分隔符解码器-解决TCP粘包
 * @Date: 2020.12.26 15:53
 **/
public class EchoClient {

    private void connect(int port, String ip) {
        //配置客户端NIO线程组
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,3000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast("frameDecoder",new LengthFieldBasedFrameDecoder(65535,0,2,0,2));
                        socketChannel.pipeline().addLast("msgpack decoder",new MsgPackDecoder());
                        socketChannel.pipeline().addLast("frameEncoder",new LengthFieldPrepender(2));
                        socketChannel.pipeline().addLast("msgpack encoder",new MsgPackEncoder());
                        socketChannel.pipeline().addLast(new EchoClientHandler());
                    }
                });

        try {
            //发起异步连接操作
            ChannelFuture f = b.connect(ip,port).sync();
            //等待客户端链路关闭
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }
    }



    public static void main(String[] args) {
        int port = 8080;
        if(args!=null && args.length>0){
            try{
                port = Integer.parseInt(args[0]);
            }catch (Exception e){

            }
        }
        new EchoClient().connect(port,"127.0.0.1");
    }
}
