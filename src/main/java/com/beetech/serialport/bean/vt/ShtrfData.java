package com.beetech.serialport.bean.vt;

import com.beetech.serialport.code.response.ReadDataResponse;

import java.text.SimpleDateFormat;

public class ShtrfData {

	public static SimpleDateFormat dateFromat = new SimpleDateFormat("yyyyMMddHHmmss");
	/**
	 * 标签ID  MAC地址
	 */
	private String rfId;
	/**
	 * 信号强度
	 */
	private Integer rssi;
	/**
	 * 时间 20180420100720
	 */
	private String time;
	private String gt;

	/**
	 * 温度  * 10的整数
	 */
	private Integer t;
	/**
	 * 湿度   * 10的整数
	 */
	private Integer h;
	/**
	 * 电压  *100 整数
	 */
	private Integer sv;
	
	public ShtrfData() {}
	public ShtrfData(ReadDataResponse readDataResponse) {
//		this.gt = dateFromat.format(readDataResponse.getGwTime());
		this.rfId = readDataResponse.getSensorId();
		this.h = Double.valueOf(readDataResponse.getRh()*10).intValue();
		this.t = Double.valueOf(readDataResponse.getTemp()*10).intValue();
		this.time  = dateFromat.format(readDataResponse.getSensorDataTime());
//		this.rssi  = readDataResponse.getRssi();
//		this.sv = Double.valueOf(readDataResponse.getSsVoltage()*1000).intValue();
	}

	public String getRfId() {
		return rfId;
	}
	public void setRfId(String rfId) {
		this.rfId = rfId;
	}
	public Integer getRssi() {
		return rssi;
	}
	public void setRssi(Integer rssi) {
		this.rssi = rssi;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}

	public String getGt() {
		return gt;
	}

	public void setGt(String gt) {
		this.gt = gt;
	}

	public Integer getT() {
		return t;
	}
	public void setT(Integer t) {
		this.t = t;
	}
	public Integer getH() {
		return h;
	}
	public void setH(Integer h) {
		this.h = h;
	}
	public Integer getSv() {
		return sv;
	}
	public void setSv(Integer sv) {
		this.sv = sv;
	}
	
}
