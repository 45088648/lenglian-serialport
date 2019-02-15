package com.beetech.serialport.utils;

public class CRC8 {

    public static char getCrc(byte[] dataBuf) {
        char bit;        // bit mask
        char crc = 0xFF; // calculated checksum
        char byteCtr;    // byte counter

        // calculates 8-Bit checksum with given polynomial
        for(byteCtr = 0; byteCtr < dataBuf.length; byteCtr++) {
            crc ^= (dataBuf[byteCtr]);
            for(bit = 8; bit > 0; --bit) {
                if((crc & 0x80)==0x80) {
                    crc =  (char) ((crc << 1) ^ 0x131);
                }
                else {
                    crc =  (char) (crc << 1);
                }
            }
        }
        return crc;
    }

    public static void main(String[] args) {
        String bufHex = "55a7";
        byte[] buf = ByteUtilities.asByteArray(bufHex);


        char crc = CRC8.getCrc(buf); // 计算前两位的CRC码
        // 65336
        System.out.println(crc);
        System.out.println(Integer.toHexString(crc));
    }
}
