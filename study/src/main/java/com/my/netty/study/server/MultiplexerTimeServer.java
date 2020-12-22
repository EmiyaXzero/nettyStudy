package com.my.netty.study.server;

import com.sun.org.apache.bcel.internal.generic.Select;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.security.Key;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author: shanghang
 * @Project:nettyStudy
 * @description:多路复用器
 * @Date: 2020.12.22 21:57
 **/

public class MultiplexerTimeServer implements Runnable{
    private Selector selector;

    private ServerSocketChannel serverSocketChannel;
    /**
     * 默认值 false
     */
    private volatile boolean stop;

    /**
     * 初始化多路复用器
     * @param port
     */
    MultiplexerTimeServer(int port) throws IOException {
        //创建多路复用器
        selector = Selector.open();
        //打开ServerSocketChannel,设置连接模式为非阻塞
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        //绑定端口
        serverSocketChannel.socket().bind(new InetSocketAddress(port),1024);
        //将serverSocketChannel注册到Reactor线程的多路复用器
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("The timeServer is start on port:" + port);
    }

    public void stop(){
        this.stop = true;
    }


    @Override
    public void run() {
        while (!stop){
            try{
                //获取数据,设置休眠时间1s
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                SelectionKey key = null;
                while (it.hasNext()){
                    key = it.next();
                    it.remove();
                    try {
                        //处理数据
                        handleInput(key);
                    }catch (Exception e){
                        if(key != null){
                            key.cancel();
                        }
                        if(key.channel() != null){
                            key.channel().close();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //多路复用器关闭后，所有注册在上面的channel 和Pipe等资源都会被自动去注册并关闭，所以不需要主动释放资源，防止重复释放
        if(selector != null){
            try{
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey key) throws IOException {
        if(key.isValid()){
            //key是有效的，开始处理
            if(key.isAcceptable()){
                //接受的新的连接
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                //获取到SocketChannel实例相当于完成了tcp三次握手
                SocketChannel sc = ssc.accept();
                //设置为非阻塞
                sc.configureBlocking(false);
                //往多路复用上面注册读事件
                sc.register(selector,SelectionKey.OP_READ);
            }
            if(key.isReadable()){
                //读数据
                SocketChannel sc = (SocketChannel) key.channel();
                //开辟1M的缓存区
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                //从缓存区取数据
                int readBytes = sc.read(readBuffer);
                if(readBytes > 0){
                    //读取到的数据>0处理，flip操作，将缓冲区当前的limit设置为postion,postion设置为0
                    readBuffer.flip();
                    byte[] bytes = new byte[readBuffer.remaining()];
                    readBuffer.get(bytes);
                    String body = new String(bytes,"UTF-8");
                    System.out.println("The time server receiver order :" + body);
                    String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)?
                            new java.util.Date(System.currentTimeMillis()).toString():"BAD ORDER";
                    doWrite(sc,currentTime);
                }else if(readBytes <0){
                    //读取到的数据<0,表示链路结束，需要关闭连接释放资源
                    key.cancel();
                    sc.close();
                }
            }
        }
    }

    private void doWrite(SocketChannel sc, String response) throws IOException {
        if(response !=null && response.trim().length()>0){
            byte[] bytes = response.getBytes();
            //缓存区
            ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
            writeBuffer.put(bytes);
            writeBuffer.flip();
            sc.write(writeBuffer);
        }
    }
}
