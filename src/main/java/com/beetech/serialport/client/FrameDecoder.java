package com.beetech.serialport.client;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import java.nio.charset.Charset;

public class FrameDecoder extends CumulativeProtocolDecoder {

    private final static Charset charset = Charset.forName("GBK");
    // 可变的IoBuffer数据缓冲区
    private IoBuffer buff = IoBuffer.allocate(100).setAutoExpand(true);

    @Override
    protected boolean doDecode(IoSession ioSession, IoBuffer ioBuffer, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {
        // 如果有消息
        while (ioBuffer.hasRemaining()) {
            // 判断消息是否是结束符
            byte b = ioBuffer.get();
            if (b == '\0') {
                buff.flip();
                byte[] bytes = new byte[buff.limit()];
                buff.get(bytes);
                String message = new String(bytes, charset);
                buff = IoBuffer.allocate(100).setAutoExpand(true);
                // 如果结束了，就写入转码后的数据
                protocolDecoderOutput.write(message);
            } else {
                buff.put(b);
            }
        }
        return false;
    }
}
