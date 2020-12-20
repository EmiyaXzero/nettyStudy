package com.my.netty.study.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author shanghang
 * @title: TimeServerHandler
 * @projectName nettyStudy
 * @description: 运行的代码
 * @date 2020.12.20-15:22
 */
public class TimeServerHandler implements Runnable{
    private Socket socket;
    TimeServerHandler(Socket socket){
        this.socket = socket;
    }
    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        try{
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            out = new PrintWriter(this.socket.getOutputStream(),true);
            String currentTime = null;
            String body = null;
            System.out.println("socket连接");
            while(true){
                body = in.readLine();
                System.out.println(body);
                if(body == null){
                    break;
                }
                System.out.println("The time server recevier order :" + body);
                currentTime ="QUERY TIME ORDER".equalsIgnoreCase(body)?new java.util.Date(System.currentTimeMillis()).toString():"BAD ORDER";
                out.println(currentTime);
            }
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
            if(this.socket != null){
                try{
                    this.socket.close();
                }catch (IOException e){

                }
                this.socket = null;
            }
        }
    }
}
