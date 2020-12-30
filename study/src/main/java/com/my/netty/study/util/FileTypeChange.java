package com.my.netty.study.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.my.netty.study.frame.http.pojo.Address;
import com.my.netty.study.frame.http.pojo.Customer;
import com.my.netty.study.frame.http.pojo.Order;
import com.my.netty.study.frame.http.pojo.Shipping;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

/**
 * @author shanghang
 * @title: FileTypeChange
 * @projectName nettyStudy
 * @description: 好像这个工具类不行啊
 * @date 2020.12.30-20:10
 */
public class FileTypeChange {
    /**
     * xml to json
     */
    public static String xml2Json(String xml) throws  IOException, DocumentException {
        JSONObject obj = new JSONObject();

        SAXReader sr = new SAXReader();
        Document doc = sr.read(new StringReader(xml));
        //获取根节点
        Element root = doc.getRootElement();
        obj.put(root.getName(),iterateElement(root));
        return obj.toString();
    }

    public static String json2xml(String json){
        com.alibaba.fastjson.JSONObject object = JSONObject.parseObject(json);
        Document document = DocumentHelper.createDocument();
        document.setXMLEncoding("UTF-8");
        for (String key : object.keySet()){
            Element root = jsonToElement(object.getJSONObject(key),key);
            document.add(root);
            break;
        }
        return document.asXML();
    }

    private static Element jsonToElement(com.alibaba.fastjson.JSONObject json, String elementName) {
        Element node = DocumentHelper.createElement(elementName);
        for (String key : json.keySet()){
            //可能只有值了
            Object object = json.get(key);
            if(object instanceof JSONObject){
                node.add( jsonToElement(json.getJSONObject(key),key));
            }else{
                Element element = DocumentHelper.createElement(key);
                element.setText(json.getString(key));
                node.add(element);
            }
        }
        return node;
    }


    /**
     * 遍历xml dom节点到map
     * @param root
     * @return
     */
    private static JSONObject iterateElement(Element root) {
       JSONObject jsonObject = new JSONObject();
       for (Object child : root.elements()){
           Element e = (Element) child;
           if(e.elements().isEmpty()){
               jsonObject.put(e.getName(),e.getTextTrim());
           }else{
               jsonObject.put(e.getName(),iterateElement(e));
           }
       }
       return jsonObject;
    }

    public static void main(String[] args) throws IOException, DocumentException, JAXBException {
        Order order = new Order();
        order.setOrderNumber("111");
        Address address = new Address();
        address.setCity("南京市");
        address.setCountry("中国");
        address.setPostCode("123321");
        address.setState("江苏省");
        address.setStreet1("龙眠大道");
        order.setBillTo(address);
        Customer customer = new Customer();
        customer.setCustomerNumber("233");
        customer.setFirstName("李");
        customer.setLastName("林峰");
        List<String> name = new ArrayList<>();
        name.add("aaa");
        name.add("bbb");
        customer.setMiddleNames(name);
        List<Customer> customers = new ArrayList<>();
        customers.add(customer);
        order.setCustomer(customers);
        order.setShipping(Shipping.INTERNATIONAL_MAIL);
        order.setBillTo(address);
        String[] ss = order.getClass().getName().split("\\.");
        JSONObject temp =  JSONObject.parseObject(JSONObject.toJSONString(order));
        JSONObject object = new JSONObject();
        object.put(ss[ss.length-1],temp);
        System.out.println(XmlPojoUtil.pojoToString(order,Order.class));
        //System.out.println(xml2Json(json2xml(object.toJSONString())));
    }
}
