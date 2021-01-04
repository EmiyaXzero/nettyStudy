package com.my.netty.study.mynetty;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import org.jboss.marshalling.ByteOutput;

import java.io.IOException;

/**
 * @Author: shanghang
 * @Project:nettyStudy
 * @description:TODO
 * @Date: 2021.01.03 20:54
 **/
@Data
public class ChannelBufferByteOutput implements ByteOutput {
    private ByteBuf byteBuf;

    ChannelBufferByteOutput(ByteBuf buf){
        this.byteBuf = buf;
    }

    @Override
    public void write(int b) throws IOException {
        byteBuf.writeByte(b);
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        byteBuf.writeBytes(bytes);
    }

    @Override
    public void write(byte[] bytes, int srcIndex, int length) throws IOException {
        byteBuf.writeBytes(bytes,srcIndex,length);
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public void flush() throws IOException {

    }

}
