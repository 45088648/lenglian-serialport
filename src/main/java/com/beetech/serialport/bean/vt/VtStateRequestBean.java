package com.beetech.serialport.bean.vt;

import com.beetech.serialport.bean.QueryConfigRealtime;

public class VtStateRequestBean {
	private Integer id = 0;
	private String v = "1.0";
	private String cmd = "STATE";
	private StateRequestBean body ;

	public VtStateRequestBean() {}
	public VtStateRequestBean(QueryConfigRealtime queryConfigRealtime) {
		this.id = queryConfigRealtime.get_id();
		this.body = new StateRequestBean(queryConfigRealtime);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getV() {
		return v;
	}

	public void setV(String v) {
		this.v = v;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public StateRequestBean getBody() {
		return body;
	}

	public void setBody(StateRequestBean body) {
		this.body = body;
	}
	
}
