package com.my.netty.study.frame.http.pojo;

import lombok.Data;

/**
 * @author shanghang
 * @title: Address
 * @projectName nettyStudy
 * @description: 地址
 * @date 2020.12.30-19:47
 */
@Data
public class Address {
    private String street1;

    private String street2;

    private String city;

    private String state;

    private String postCode;

    private String country;
}
