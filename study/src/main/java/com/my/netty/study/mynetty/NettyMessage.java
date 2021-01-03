package com.my.netty.study.mynetty;

import lombok.Data;

/**
 * @Author: shanghang
 * @Project:nettyStudy
 * @description:自定义协议
 * @Date: 2021.01.03 20:31
 **/
@Data
public final class NettyMessage {
    private Header header;

    private Object body;


    @Override
    public String toString() {
        return "NettyMessage{" +
                "header=" + header +
                ", body=" + body +
                '}';
    }
}
