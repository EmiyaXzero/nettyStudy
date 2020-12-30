package com.my.netty.study.util;

import javax.xml.bind.*;
import java.io.*;

/**
 * @Author: shanghang
 * @Project:nettyStudy
 * @description:TODO
 * @Date: 2020.12.30 22:26
 **/
public class XmlPojoUtil {
    public static <T> T xmlFileToObject(String xmlFilePath, Class<T> clazz) {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        try (InputStream inputStream = contextClassLoader.getResourceAsStream(xmlFilePath)) {
            JAXBContext context = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            T result = (T) unmarshaller.unmarshal(inputStream);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("convert xml to POJO failure!", e);
        }
    }

    public static <T> T xmlStringToObject(String xmlString, Class<T> clazz) {
        try (Reader reader = new StringReader(xmlString)) {
            JAXBContext context = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            T result = (T) unmarshaller.unmarshal(reader);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("convert xml string to POJO failure!", e);
        }
    }

//    public String pojoToString(Object pojo) throws JAXBException {
//        // 首先创建 JAXBContext
//        JAXBContext context = JAXBContext.newInstance(Object.class);
//        Marshaller marshaller = context.createMarshaller();
//        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
//    }
}
