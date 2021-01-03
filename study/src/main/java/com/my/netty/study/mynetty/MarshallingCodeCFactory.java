package com.my.netty.study.mynetty;

import org.jboss.marshalling.*;

import java.io.IOException;

/**
 * @Author: shanghang
 * @Project:nettyStudy
 * @description:marshalling工厂类
 * @Date: 2021.01.03 21:00
 **/
public class MarshallingCodeCFactory {


    public static Marshaller buildMarshalling() throws IOException {
        final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
        final MarshallingConfiguration marshallingConfiguration =new MarshallingConfiguration();
        marshallingConfiguration.setVersion(5);
        Marshaller marshaller = marshallerFactory.createMarshaller(marshallingConfiguration);
        return marshaller;
    }
    public static Unmarshaller buildUnmarshalling() throws IOException {
        final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
        final MarshallingConfiguration marshallingConfiguration =new MarshallingConfiguration();
        marshallingConfiguration.setVersion(5);
        final Unmarshaller unmarshaller = marshallerFactory.createUnmarshaller(marshallingConfiguration);
        return unmarshaller;
    }

}
