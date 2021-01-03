package com.my.netty.study.mynetty;

import io.netty.buffer.ByteBuf;
import org.jboss.marshalling.ByteInput;

import java.io.IOException;

/**
 * @Author: shanghang
 * @Project:nettyStudy
 * @description:TODO
 * @Date: 2021.01.03 21:10
 **/
public class ChannelBufferByteInput implements ByteInput {
    private ByteBuf buf ;

    public ChannelBufferByteInput(ByteBuf buf) {
        this.buf = buf;
    }

    @Override
    public int read() throws IOException {
        if(buf.isReadable()){
            return buf.readByte() & 0xff;
        }
        return -1;
    }

    @Override
    public int read(byte[] bytes) throws IOException {
        return read(bytes,0,bytes.length);
    }

    @Override
    public int read(byte[] bytes, int dstIndex, int length) throws IOException {
        int available = available();
        if(available == 0){
            return -1;
        }
        length = Math.min(available,length);
        buf.readBytes(bytes, dstIndex, length);
        return length;
    }

    @Override
    public int available() throws IOException {
        return buf.readableBytes();
    }

    @Override
    public long skip(long bytes) throws IOException {
       int readable =buf.readableBytes();
       if(readable<bytes){
           bytes = readable;
       }
        buf.readerIndex((int) (buf.readerIndex()+bytes));
       return bytes;
    }

    @Override
    public void close() throws IOException {

    }
}
