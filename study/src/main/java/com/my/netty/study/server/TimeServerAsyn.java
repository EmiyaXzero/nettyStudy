package com.my.netty.study.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author shanghang
 * @title: TimeServer
 * @projectName nettyStudy
 * @description: 服务端监听伪异步
 * @date 2020.12.20-15:15
 */
public class TimeServerAsyn {
    public static void main(String[] args) throws IOException {
        int port = 8080;
        if(args != null && args.length > 0){
            try {
                port = Integer.valueOf(args[0]);
            }catch (Exception e){

            }
        }
        ServerSocket server = null;
        try {
            //服务端监听端口
            server = new ServerSocket(port);
            System.out.println("端口：" + port + "监听成功");
            Socket socket = null;
            //创建线程池处理客户端请求
            TimeServerHandlerExecutePool singleExcutor = new TimeServerHandlerExecutePool(50,10000);
            while (true){
                socket = server.accept();
                //有一个socket请求就往线程池里面塞
                singleExcutor.execute(new TimeServerHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(server != null){
                server.close();
                server = null;
            }
        }
    }
}
