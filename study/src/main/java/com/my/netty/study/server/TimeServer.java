package com.my.netty.study.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author shanghang
 * @title: TimeServer
 * @projectName nettyStudy
 * @description: 服务端监听
 * @date 2020.12.20-15:15
 */
public class TimeServer {
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
            //监听端口
            server = new ServerSocket(port);
            Socket socket = null;
            while (true){
                //一直等待
                socket = server.accept();
                new Thread(new TimeServerHandler(socket)).start();
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
