package com.my.netty.study.mynetty;

import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.ByteInput;
import org.jboss.marshalling.Unmarshaller;

import java.io.IOException;

/**
 * @Author: shanghang
 * @Project:nettyStudy
 * @description:TODO
 * @Date: 2021.01.03 21:07
 **/
public class MarshallingDecoder {
    private final Unmarshaller unmarshaller;

    MarshallingDecoder() throws IOException {
        unmarshaller = MarshallingCodeCFactory.buildUnmarshalling();
    }

    protected Object decode(ByteBuf in) throws IOException, ClassNotFoundException {
        int objectSize = in.readInt();
        ByteBuf buf = in.slice(in.readerIndex(),objectSize);
        ByteInput byteInput = new ChannelBufferByteInput(buf);
        try{
            unmarshaller.start(byteInput);
            Object obj = unmarshaller.readObject();
            unmarshaller.finish();
            in.readerIndex(in.readerIndex()+objectSize);
            return obj;
        }finally {
            unmarshaller.close();
        }
    }
}
