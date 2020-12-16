package com.my.netty.study.nettycompant;



import sun.misc.Signal;
import java.util.concurrent.TimeUnit;

/**
 * @author shanghang
 * @title: SignalDemo
 * @projectName nettyStudy
 * @description: 信号量学习
 * @date 2020.12.15-23:41
 */
public class SignalDemo {
    public static void main(String[] args) {
        Signal sig = new Signal(getOSSignalType());
        Signal.handle(sig,(s)->{
            System.out.println("Signal handle start..");
            try{
                TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            System.out.println("ShutdownHook execute start...");
            System.out.println("Netty NioEventLoopGroop shutdownGracefully");
            try{
                TimeUnit.SECONDS.sleep(3);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            System.out.println("ShutdownHook execute end ...");
        },""));
    }

    private static String getOSSignalType() {
        //判断当前操作系统，win选择INT信号，linux选择TERM
        return System.getProperties().getProperty("os.name").toLowerCase().startsWith("win")?"INT":"TERM";
    }

}
