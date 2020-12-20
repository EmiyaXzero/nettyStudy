package com.my.netty.study.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author shanghang
 * @title: TimeClient
 * @projectName nettyStudy
 * @description: time客户端
 * @date 2020.12.20-15:30
 */
public class TimeClientAsyn {
    static int port = 8080;

    public static void main(String[] args) {
        for (int i = 0;i<10;i++){
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    Socket socket = null;
                    BufferedReader in = null;
                    PrintWriter out = null;
                    try {
                        socket = new Socket("127.0.0.1",port);
                        //客户端的输入，服务端的输出
                        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        //客户端的输出，服务端的输入
                        out = new PrintWriter(socket.getOutputStream(),true);
                        out.println("QUERY TIME ORDER");
                        String resp = in.readLine();
                        System.out.println("服务端输出:"+resp);

                    }catch (Exception e){

                    }finally {
                        if(in != null){
                            try{
                                in.close();
                            }catch (IOException e){

                            }finally {
                                in = null;
                            }
                        }
                        if(out != null){
                            out.close();
                            out = null;
                        }
                        if(socket != null){
                            try{
                                socket.close();
                            }catch (IOException e){

                            }
                            socket = null;
                        }
                    }
                }
            });
            thread.start();
        }
    }


}
