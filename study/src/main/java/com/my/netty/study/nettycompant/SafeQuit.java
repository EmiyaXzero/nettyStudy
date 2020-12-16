package com.my.netty.study.nettycompant;

import java.util.concurrent.TimeUnit;

/**
 * @author shanghang
 * @title: SafeQuit
 * @projectName nettyStudy
 * @description: 安全退出
 * @date 2020.12.15-23:12
 */
public class SafeQuit {
    public static void main(String[] args) throws InterruptedException {
        Runtime.getRuntime().addShutdownHook(new Thread(
                //lamda表达式实现Runnable的run抽象函数
                ()->{
                    System.out.println("ShutdownHook execut start...");
                    System.out.println("Netty NioEventLoopGroup shutdownGracefully");
                    try{
                        TimeUnit.SECONDS.sleep(3);
                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {

                    }
                    System.out.println("ShutdownHook execut end...");
                },"Watch Thread")
        );
        TimeUnit.SECONDS.sleep(7);
        System.exit(0);
    }
}
