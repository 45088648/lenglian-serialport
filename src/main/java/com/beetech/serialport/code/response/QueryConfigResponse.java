package com.beetech.serialport.code.response;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import com.ab.db.orm.annotation.Column;
import com.ab.db.orm.annotation.Id;
import com.ab.db.orm.annotation.Table;
import com.beetech.serialport.code.BaseResponse;
import com.beetech.serialport.utils.ByteUtilities;

@Table(name = "query_config_t")
public class QueryConfigResponse extends BaseResponse {
	// ID @Id主键,int类型,数据库建表时此字段会设为自增长
	@Id
	@Column(name = "_id")
	private int _id;

	@Column(name = "hard_ver")
	private String hardVer; // 硬件版本

	@Column(name = "soft_ver")
	private String softVer; // 软件版本

	@Column(name = "customer")
	private int customer; //客户码

	@Column(name = "debug")
	private int debug; // debug固定值

	@Column(name = "category")
	private int category; // 分类码

	@Column(name = "interval")
	private int interval; // 时间间隔

	@Column(name = "calendar", type="DATETIME")
	private Date calendar; // 采集时间,BCD码，格式：“16年05月20日17时12分46秒

	@Column(name = "pattern")
	private int pattern; //工作模式

	@Column(name = "bps")
	private int bps; // 传输速率

	@Column(name = "channel")
	private int channel; // 频段

	@Column(name = "ram_data")
	private int ramData; // RAM数据

	@Column(name = "front")
	private int front; // pflash 循环队列的读指针，最大值是1023

	@Column(name = "rear")
	private int rear; // pflash 循环队列的写指针，最大值是1023

	@Column(name = "p_flash_length")
	private int pflashLength; // pflash 循环队列中已存数据的数目，最大值是1023。

	@Column(name = "send_ok")
	private int sendOk; // 数据包发送成功标识位： 0 = 失败； 1 = 成功； other = 未定义

	@Column(name = "gw_voltage")
	private double gwVoltage; //计算公式：U = x*4/1023, 单位：V，其中，x = byte1*256+byte2

	@Column(name = "gw_id")
	public String gwId; // 网关序列号(小模块网关编号 )

	@Column(name="input_time", type="DATETIME")
	Date inputTime;

    public QueryConfigResponse(){}
    public QueryConfigResponse(byte[] buf) {
        super(buf);
        inputTime = new Date();
    }

	@Override
	public void unpack() {
		if(buf == null || buf.length == 0) {
			return;
		}
		
		int start = 0;
		this.begin  = ByteUtilities.makeIntFromByte2(buf, start); start = start + 2;
		this.packLen  = ByteUtilities.toUnsignedInt(buf[start]); start = start + 1;
		this.cmd = ByteUtilities.toUnsignedInt(buf[start]); start = start + 1;
		this.gwId = Integer.toHexString(ByteUtilities.makeIntFromByte4(buf, start)); start = start + 4;
		this.hardVer = Integer.toHexString(ByteUtilities.makeIntFromByte4(buf, start)); start = start + 4;
		this.softVer = Integer.toHexString(ByteUtilities.makeIntFromByte2(buf, start)); start = start + 2;
		this.customer = ByteUtilities.makeIntFromByte2(buf, start); start = start + 2;
		this.debug = ByteUtilities.makeIntFromByte2(buf, start); start = start + 2;
		this.category = ByteUtilities.toUnsignedInt(buf[start]); start = start + 1;
		this.interval = ByteUtilities.makeIntFromByte2(buf, start); start = start + 2;
		try {
			this.calendar = dateFromat.parse(ByteUtilities.bcd2Str(ByteUtilities.subBytes(buf, start, 6))); start = start + 6;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.pattern = ByteUtilities.toUnsignedInt(buf[start]); start = start + 1;
		this.bps = ByteUtilities.toUnsignedInt(buf[start]); start = start + 1;
		this.channel = ByteUtilities.toUnsignedInt(buf[start]); start = start + 1;
		this.ramData = ByteUtilities.toUnsignedInt(buf[start]); start = start + 1;
		this.front = ByteUtilities.makeIntFromByte2(buf, start); start = start + 2;
		this.rear = ByteUtilities.makeIntFromByte2(buf, start); start = start + 2;
		this.pflashLength = ByteUtilities.makeIntFromByte2(buf, start); start = start + 2;
		this.sendOk = ByteUtilities.toUnsignedInt(buf[start]); start = start + 1;
		BigDecimal gwVoltageBd = new BigDecimal((ByteUtilities.toUnsignedInt(buf[start])*256+ByteUtilities.toUnsignedInt(buf[start+1]))*4*1.0/1023);
		this.gwVoltage = gwVoltageBd.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue(); start = start + 2;
		this.crc  = ByteUtilities.makeIntFromByte2(buf, start); start = start + 2;
		this.end  = ByteUtilities.makeIntFromByte2(buf, start); start = start + 2;
		
	}

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

	public String getHardVer() {
		return hardVer;
	}


	public void setHardVer(String hardVer) {
		this.hardVer = hardVer;
	}


	public String getSoftVer() {
		return softVer;
	}


	public void setSoftVer(String softVer) {
		this.softVer = softVer;
	}


	public int getCustomer() {
		return customer;
	}


	public void setCustomer(int customer) {
		this.customer = customer;
	}


	public int getDebug() {
		return debug;
	}


	public void setDebug(int debug) {
		this.debug = debug;
	}


	public int getCategory() {
		return category;
	}


	public void setCategory(int category) {
		this.category = category;
	}


	public int getInterval() {
		return interval;
	}


	public void setInterval(int interval) {
		this.interval = interval;
	}


	public Date getCalendar() {
		return calendar;
	}


	public void setCalendar(Date calendar) {
		this.calendar = calendar;
	}


	public int getPattern() {
		return pattern;
	}


	public void setPattern(int pattern) {
		this.pattern = pattern;
	}


	public int getBps() {
		return bps;
	}


	public void setBps(int bps) {
		this.bps = bps;
	}


	public int getChannel() {
		return channel;
	}


	public void setChannel(int channel) {
		this.channel = channel;
	}


	public int getRamData() {
		return ramData;
	}


	public void setRamData(int ramData) {
		this.ramData = ramData;
	}


	public int getFront() {
		return front;
	}


	public void setFront(int front) {
		this.front = front;
	}


	public int getRear() {
		return rear;
	}


	public void setRear(int rear) {
		this.rear = rear;
	}


	public int getPflashLength() {
		return pflashLength;
	}


	public void setPflashLength(int pflashLength) {
		this.pflashLength = pflashLength;
	}


	public int getSendOk() {
		return sendOk;
	}


	public void setSendOk(int sendOk) {
		this.sendOk = sendOk;
	}


	public double getGwVoltage() {
		return gwVoltage;
	}


	public void setGwVoltage(double gwVoltage) {
		this.gwVoltage = gwVoltage;
	}

	public String getGwId() {
		return gwId;
	}

	public void setGwId(String gwId) {
		this.gwId = gwId;
	}

	public Date getInputTime() {
		return inputTime;
	}

	public void setInputTime(Date inputTime) {
		this.inputTime = inputTime;
	}
}
