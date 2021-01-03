package com.my.netty.study.mynetty;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: shanghang
 * @Project:nettyStudy
 * @description:消息头
 * @Date: 2021.01.03 20:33
 **/
@Data
public class Header {
    private int crcCode = 0xabef0101;
    /**
     * 消息长度
     */
    private int length;
    /**
     * 会话ID
     */
    private long sessionId;
    /**
     * 消息类型
     */
    private byte type;
    /**
     * 优先级
     */
    private byte priority;

    private Map<String,Object> attachment = new HashMap<>();

    @Override
    public String toString() {
        return "Header{" +
                "crcCode=" + crcCode +
                ", length=" + length +
                ", sessionId=" + sessionId +
                ", type=" + type +
                ", priority=" + priority +
                ", attachment=" + attachment +
                '}';
    }
}
