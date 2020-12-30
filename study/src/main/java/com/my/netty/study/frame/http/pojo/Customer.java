package com.my.netty.study.frame.http.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author shanghang
 * @title: Customer
 * @projectName nettyStudy
 * @description: 顾客
 * @date 2020.12.30-19:45
 */
@Data
public class Customer {
    private String customerNumber;

    private String firstName;

    private String lastName;

    private List<String> middleNames;
}
