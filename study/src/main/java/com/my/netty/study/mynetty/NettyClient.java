package com.my.netty.study.mynetty;

import com.my.netty.study.NettyConstant;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Author: shanghang
 * @Project:nettyStudy
 * @description:TODO
 * @Date: 2021.01.03 23:46
 **/
public class NettyClient {
    private ScheduledExecutorService executor =
            Executors.newScheduledThreadPool(1);
    private EventLoopGroup group = new NioEventLoopGroup();

    public void connect(int port,String host) throws Exception{
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(
                                    new NettyMessageDecoder(1024 * 1024, 4, 4)
                            );
                            ch.pipeline().addLast("MessageEncoder",new NettyMessageEncoder());
                            ch.pipeline().addLast("readTimeoutHandler",new ReadTimeoutHandler(50));
                            //握手
                            ch.pipeline().addLast("LoginAuthHandler",new LoginAuthReqHandler());
                            //心跳
                            ch.pipeline().addLast("HeartBeatHandler",new HeaderBeatReqHandler());
                        }
                    });
            ChannelFuture future = b.connect(
                    new InetSocketAddress(host,port),
                    new InetSocketAddress(NettyConstant.LOCALIP,NettyConstant.LOCAL_PORT)
            ).sync();
           // ChannelFuture future = b.connect(host,port).sync();
            future.channel().closeFuture().sync();
        }finally {
            //所有资源释放成功后，清空资源，再次发起重连
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        TimeUnit.SECONDS.sleep(5);
                        connect(NettyConstant.PORT,NettyConstant.REMOTEIP);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void main(String[] args) throws Exception {
        new NettyClient().connect(8080,"127.0.0.1");
    }
}
