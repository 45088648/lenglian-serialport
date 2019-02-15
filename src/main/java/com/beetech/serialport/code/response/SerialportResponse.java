package com.beetech.serialport.code.response;

import com.beetech.serialport.code.BaseResponse;

import java.io.Serializable;

/**
 * （3）固定数据格式：
 * ①0x55  ②0x0b  ③0x00  ④MSB  LSB  CRC  ⑤MSB  LSB  CRC  ⑥0xnn
 * ①为包头，表示向主机返回数据
 * ②为返回总字节数，固定为10
 * ③为探头编号,0x00探头A，0x01探头B
 * ④为采集到的16位温度数据（高8位字节在前）及8位CRC校验码
 * ⑤为采集到的16位湿度数据（高8位字节在前）及8位CRC校验码
 * ⑥为整个数据包的校验码，前面所有字节累加求和取反加1
 */
public class SerialportResponse  extends BaseResponse implements Serializable {

    @Override
    public void unpack() {

    }
}
