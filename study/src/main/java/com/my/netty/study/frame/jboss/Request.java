package com.my.netty.study.frame.jboss;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: shanghang
 * @Project:nettyStudy
 * @description:TODO
 * @Date: 2020.12.28 22:29
 **/
@Data
public class Request implements Serializable {


    private static final long serialVersionUID = 8933887102165597903L;
    private String name;
    private int age;

    @Override
    public String toString() {
        return "Request{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
