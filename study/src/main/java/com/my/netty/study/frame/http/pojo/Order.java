package com.my.netty.study.frame.http.pojo;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author shanghang
 * @title: Order
 * @projectName nettyStudy
 * @description: order
 * @date 2020.12.30-19:44
 */
@Data
@XmlRootElement(name="BUSI_INFO")
@XmlAccessorType(XmlAccessType.FIELD)
public class Order {
    private String orderNumber;

    private List<Customer> customer;

    private Address billTo;

    private Shipping shipping;


}
