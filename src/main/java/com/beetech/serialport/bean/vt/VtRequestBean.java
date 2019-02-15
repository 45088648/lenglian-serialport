package com.beetech.serialport.bean.vt;

import com.beetech.serialport.code.response.ReadDataResponse;
import java.util.ArrayList;
import java.util.List;

public class VtRequestBean {
	private Integer id = 0;
	private String v = "1.0";
	private String cmd = "SHTRF";
	private ShtrfRequestBean body ;
	
	public VtRequestBean() {}
	public VtRequestBean(ReadDataResponse readDataResponse) {
		this.id = readDataResponse.get_id();
		this.body = new ShtrfRequestBean(readDataResponse);
	}

	public VtRequestBean(List<ReadDataResponse> readDataResponseList) {
		this.body = new ShtrfRequestBean();
		List<ShtrfData> data = new ArrayList<>();

		for (ReadDataResponse readDataResponse:readDataResponseList) {
			ShtrfData shtrfData = new ShtrfData(readDataResponse);
			data.add(shtrfData);
		}

		this.body.setData(data);
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

	public ShtrfRequestBean getBody() {
		return body;
	}

	public void setBody(ShtrfRequestBean body) {
		this.body = body;
	}
	
}
