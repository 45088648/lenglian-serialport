package com.beetech.serialport.code.response;

import java.io.Serializable;
import java.util.Date;
import com.ab.db.orm.annotation.Column;
import com.ab.db.orm.annotation.Id;
import com.ab.db.orm.annotation.Table;
import com.beetech.serialport.code.BaseResponse;
import com.beetech.serialport.utils.ByteUtilities;
import com.beetech.serialport.utils.CRC8;

/**
 ①0x55  ②0x0b  ③0x00  ④0x14 ⑤MSB  LSB  CRC  ⑥MSB  LSB  CRC  ⑦0xnn
 ①为包头，表示向主机返回数据
 ②为返回总字节数，固定为11字节
 ③为探头编号,0x00探头A，0x01探头B
 ④传感器型号 0x11：STS2x、0x12：STS3x、0x13：SHT2x、0x14：SHT3x
 ⑤为采集到的16位温度数据（高8位字节在前）及8位CRC校验码
 ⑥为采集到的16位湿度数据（高8位字节在前）及8位CRC校验码
 ⑦为整个数据包的校验码，前面所有字节累加求和取反加1
 */
@Table(name = "read_data_t")
public class ReadDataResponse extends BaseResponse implements Serializable {
	// ID @Id主键,int类型,数据库建表时此字段会设为自增长
	@Id
	@Column(name = "_id")
	private int _id;

	@Column(name = "sensor_id", type = "String")
	private String sensorId; //传感器ID, 传感器编号

	@Column(name = "sensor_data_time", type="DATETIME")
	private Date sensorDataTime; // 本条数据的采集时间，BCD码，格式：“年 月 日 时 分 秒”。

	@Column(name = "temp")
	private double temp; //

    @Column(name = "temp1")
	private double temp1; //

	@Column(name = "rh")
	private double rh; // 湿度

    @Column(name = "rh1")
	private double rh1; // 湿度

	@Column(name="input_time", type="DATETIME")
	Date inputTime;

	@Column(name = "send_flag")
	private int sendFlag;

	public ReadDataResponse(){}
	public ReadDataResponse(Date sensorDataTime, double temp){
		this.sensorDataTime = sensorDataTime;
		this.temp = temp;
	}
	public ReadDataResponse(byte[] buf) {
		super(buf);
		inputTime = new Date();
	}

	@Override
	public void unpack() {
		if(buf == null || buf.length == 0) {
			return;
		}

		Date curTime = new Date();
		sensorDataTime = curTime;
		inputTime = curTime;

		boolean isTempHasVal = false;
		boolean isTemp1HasVal = false;
		boolean isRhHasVal = false;
		boolean isRh1HasVal = false;
		int len = buf.length;
		int start = 0;
        while(start + 1 < len){
            do{
                if(len - start > 2){
                    begin = ByteUtilities.makeIntFromByte2(buf, start); start = start + 2;
                }
            } while(begin != 0x550b);

            byte[] packSerailport = new byte[11];
            System.arraycopy(buf, start-2, packSerailport, 0, 11);
			int start1 = 2;
            int proNo = ByteUtilities.toUnsignedInt(packSerailport[start1]); start1 = start1 + 1;
            int proType = ByteUtilities.toUnsignedInt(packSerailport[start1]); start1 = start1 + 1;

			byte[] tempByte = new byte[]{packSerailport[start1], packSerailport[start1+1]};
            int tempInt = ByteUtilities.makeIntFromByte2(packSerailport, start1);  start1 = start1 + 2;
            int crc_temp = ByteUtilities.toUnsignedInt(packSerailport[start1]); start1 = start1 + 1;

			byte[] rhByte = new byte[]{packSerailport[start1], packSerailport[start1+1]};
            int rhInt = ByteUtilities.makeIntFromByte2(packSerailport, start1); start1 = start1 + 2;
            int crc_rh = ByteUtilities.toUnsignedInt(packSerailport[start1]); start1 = start1 + 1;


            byte check = packSerailport[start1]; start1 = start1 + 1;
			start = start + start1 - 2;

			int cal_crc_temp = CRC8.getCrc(tempByte);
			int cal_crc_rh = CRC8.getCrc(rhByte);
            byte cal_check = getCheck(packSerailport);
			if(check == cal_check){

				if(crc_temp == cal_crc_temp){
					double temp = calTemp(tempInt, proType);
					if(proNo == 0x00){
						this.temp = temp;
						isTempHasVal = true;
					} else if (proNo == 0x01){
						this.temp1 = temp;
						isTemp1HasVal = true;
					}
				}

				if(crc_rh == cal_crc_rh){
					double rh = calRh(rhInt, proType);
					if(proNo == 0x00){
						this.rh = rh;
						isRhHasVal = true;
					} else if (proNo == 0x01){
						this.rh1 = rh;
						isRh1HasVal = true;
					}
				}

            }

        }

	}


    public static byte getCheck(byte[] packBuf){
		int sum = 0;
	    for(int i = 0; i < packBuf.length-1; i++){
			sum += ByteUtilities.toUnsignedInt(packBuf[i]);
        }
		byte check = (byte)~sum;
	    check += 1;
	    return check;
    }

	// 根据探头型号处理温度值
	public static double calTemp(int temp, int proType){
		if((temp & 0x8000) == 0x8000){
			temp = temp-0xfff - 1;
		}

		switch (proType){
			case 0x11: // STS2x
				temp = (temp * 17572 / 65536 - 4685);
				break;
			case 0x12: // STS3x
				temp = (temp * 17500 / 65535 - 4500);
				break;

			case 0x13: // SHT2x
				temp = (temp * 17572 / 65536 - 4685);
				break;

			case 0x14: // SHT3x
				temp = (temp * 17500 / 65535 - 4500);
				break;
		}
		return temp/100;
	}

	// 根据探头型号处理湿度值
	public static double calRh(int rh, int proType){
		switch (proType){
			case 0x13: // SHT2x
				rh = (rh * 12500 / 65536 - 600);
				break;

			case 0x14: // SHT3x
				rh = (rh * 10000 / 65535);
				break;
		}
		return rh/100;
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getSensorId() {
		return sensorId;
	}

	public void setSensorId(String sensorId) {
		this.sensorId = sensorId;
	}

	public int getLoadLen() {
		return loadLen;
	}

	public void setLoadLen(int loadLen) {
		this.loadLen = loadLen;
	}

	public Date getSensorDataTime() {
		return sensorDataTime;
	}

	public void setSensorDataTime(Date sensorDataTime) {
		this.sensorDataTime = sensorDataTime;
	}

	public double getTemp() {
		return temp;
	}

	public void setTemp(double temp) {
		this.temp = temp;
	}

	public double getRh() {
		return rh;
	}

	public void setRh(double rh) {
		this.rh = rh;
	}

    public double getTemp1() {
        return temp1;
    }

    public void setTemp1(double temp1) {
        this.temp1 = temp1;
    }

    public double getRh1() {
        return rh1;
    }

    public void setRh1(double rh1) {
        this.rh1 = rh1;
    }

	public Date getInputTime() {
		return inputTime;
	}

	public void setInputTime(Date inputTime) {
		this.inputTime = inputTime;
	}

	public int getSendFlag() {
		return sendFlag;
	}

	public void setSendFlag(int sendFlag) {
		this.sendFlag = sendFlag;
	}

	public static void main(String[] args) {
		String bufHex = "550b001454ef0a7c2e97fe550b011455a77a7ab3b830550b001454ef0a7c1e5253550b011455a28f7ab3b820550b001454fabc7c31fadb550b0114559d647a8c53dc550b001454fabc7c19c528550b0114559d647aa2ca4f550b001454f4a37c1f63a3550b01145598917a9b8771550b001454ef0a7c2e97fe550b0114559d647ab1da30550b001454fabc7c2831ad550b011455a77a7a95986e550b001454f4a37c013fe5550b0114559d647aab42ce550b001454f4a37c30cb2a550b0114559d647a9c100f550b001454fabc7c31fadb550b0114559d647ab1da30550b001454f4a37c1f63a3550b0114559d647aab42ce550b001454f4a37c18f419550b01145598917aaa7376550b001454fabc7c21b92c550b011455a28f7a9c10df550b001454ef0a7c1e5253550b0114559d647a9c100f550b001454f4a37c30cb2a550b0114559d647a9c100f550b001454ff497c2a53f7550b0114559d647aab42ce550b001454ff497c1ba7b2550b0114559d647a933eea550b001454f4a37c1f63a3550b0114559d647a933eea550b001454f4a37c18f419550b0114559d647aa2ca4f550b001454f4a37c30cb2a550b011455a28f7a9c10df550b001454f4a37c366d82550b0114559d647a9c100f550b001454f4a37c18f419550b01145598917a9b8771550b001454fabc7c2831ad550b0114559d647a9c100f550b001454ef0a7c271f7d550b011455a28f7a8c53ac550b001454fabc7c122fc5550b011455a28f7a8c53ac550b001454fabc7c19c528550b011455a28f7a95985e550b001454fabc7c19c528550b011455a28f7a9c10df550b001454fabc7c2831ad550b011455ada17aaeb709550b001454ef0a7bfaf9d1550b01145598917aa19959550b001454f4a37c098696550b011455a77a7a95986e550b001454f4a37c098696550b011455a28f7a7de02e";
		byte[] buf = ByteUtilities.asByteArray(bufHex);

		byte[] dataBuf = ByteUtilities.asByteArray(bufHex);
		byte check = getCheck(dataBuf);
		System.out.println(ByteUtilities.toUnsignedInt(check));
		byte a = (byte)976;
		a = (byte)~a;
		System.out.println(ByteUtilities.toUnsignedInt(a));

		ReadDataResponse readDataResponse = new ReadDataResponse(buf);
		readDataResponse.unpack();
		System.out.println(readDataResponse.getTemp()+"~"+readDataResponse.getTemp1()+"~"+readDataResponse.getRh()+"~"+readDataResponse.getRh1());

	}

}
