package com.my.netty.study.serial;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

/**
 * @author shanghang
 * @title: TestUserInfo
 * @projectName nettyStudy
 * @description: 序列化效率对比
 * @date 2020.12.27-15:54
 */
@Slf4j
public class TestUserInfo {
    public static void main(String[] args) throws IOException {
        //doLength();
        doCast();
    }

    /**
     * 序列化对比--码流大小维度
     * @throws IOException
     */
    public static void doLength() throws IOException {
        UserInfo userInfo = new UserInfo();
        userInfo.buildUserId(100).buildUserName("张三");
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(userInfo);
        os.flush();
        os.close();
        byte[] b = bos.toByteArray();
        log.error("The jdk serializable length is "+ b.length);
        bos.close();;
        log.error("-----------------------------------");
        log.error("The byte array serializable length is :"+userInfo.codeC().length);
    }

    public static void doCast() throws IOException{
        UserInfo userInfo = new UserInfo();
        userInfo.buildUserId(100).buildUserName("张三");
        int loop = 1000000;
        ByteArrayOutputStream bos = null;
        ObjectOutputStream os = null;
        long jdkSerStart = System.currentTimeMillis();
        for (int i = 0; i < loop; i++) {
            bos = new ByteArrayOutputStream();
            os = new ObjectOutputStream(bos);
            os.writeObject(userInfo);
            os.flush();
            os.close();
            byte[] b = bos.toByteArray();
            bos.close();
        }
        log.error("The jdk cost time :" + (System.currentTimeMillis()-jdkSerStart)+": ms");

        log.error("----------------------------------------------------");

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        jdkSerStart = System.currentTimeMillis();

        for (int i = 0 ;i<loop;i++){
            byte[] b = userInfo.codeC(buffer);
        }

        log.error("The byte array cost time :" + (System.currentTimeMillis()-jdkSerStart)+": ms");


    }
}
