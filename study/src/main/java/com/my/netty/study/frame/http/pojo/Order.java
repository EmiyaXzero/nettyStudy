package com.my.netty.study.frame.http.pojo;

import lombok.Data;

/**
 * @author shanghang
 * @title: Order
 * @projectName nettyStudy
 * @description: order
 * @date 2020.12.30-19:44
 */
@Data
public class Order {
    private String orderNumber;

    private Customer customer;

    private Address billTo;

    private Shipping shipping;


}
