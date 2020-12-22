package com.my.netty.study.server;

/**
 * @Author: shanghang
 * @Project:nettyStudy
 * @description:Nio客户端示例
 * @Date: 2020.12.22 23:09
 **/
public class NioClient {
    public static void main(String[] args) {
        int port = 8080;
        if(args!=null && args.length>0){
            try{
                port = Integer.parseInt(args[0]);
            }catch (Exception e){

            }
        }
        new Thread(new TimeClientHandle("127.0.0.1",port),"TimeCilent-001").start();
    }
}
