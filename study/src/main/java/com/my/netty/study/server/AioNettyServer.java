package com.my.netty.study.server;

/**
 * @Author: shanghang
 * @Project:nettyStudy
 * @description:AioNettyServer配置
 * @Date: 2020.12.24 23:43
 **/
public class AioNettyServer {
    public static void main(String[] args) {
        int port = 8080;
        if(args!=null && args.length>0){
            try{
                port = Integer.parseInt(args[0]);
            }catch (Exception e){

            }
        }
        AsyncTimeServerHandler timeSever = new AsyncTimeServerHandler(port);
        new Thread(timeSever,"AIO-AsyncTimeServerHandler-001");
    }
}
