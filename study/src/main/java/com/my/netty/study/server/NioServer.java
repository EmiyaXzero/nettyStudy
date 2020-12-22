package com.my.netty.study.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * @Author: shanghang
 * @Project:nettyStudy
 * @description:NIOServer学习
 * @Date: 2020.12.22 21:45
 **/
public class NioServer {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        if(args!=null && args.length>0){
            try{
                port = Integer.parseInt(args[0]);
            }catch (Exception e){

            }
        }
        //创建一个多路复用器
        MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);
        new Thread(timeServer,"NIO-MultiplexerTimeServer-001").start();
    }

}
