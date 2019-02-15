package com.beetech.serialport.client;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import java.nio.charset.Charset;

public class FrameEncoder implements ProtocolEncoder {
    private final static Charset charset = Charset.forName("GBK");

    @Override
    public void encode(IoSession ioSession, Object message, ProtocolEncoderOutput protocolEncoderOutput) throws Exception {
        IoBuffer buff = IoBuffer.allocate(100).setAutoExpand(true);
        buff.putString(message.toString(), charset.newEncoder());
        // put 当前系统默认换行符
        buff.putString("\0", charset.newEncoder());
        // 为下一次读取数据做准备
        buff.flip();
        protocolEncoderOutput.write(buff);
    }

    @Override
    public void dispose(IoSession ioSession) throws Exception {

    }
}
