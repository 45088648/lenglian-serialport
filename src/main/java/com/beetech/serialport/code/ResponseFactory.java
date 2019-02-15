package com.beetech.serialport.code;

import com.beetech.serialport.code.response.QueryConfigResponse;
import com.beetech.serialport.code.response.ReadDataResponse;
import com.beetech.serialport.code.response.SetTimeResponse;

public class ResponseFactory {

	public static BaseResponse getResponse(byte[] buf) {
		if(buf == null || buf.length < 4) {
			return null;
		}
		int cmd = buf[3];
		switch (cmd) {
			case 1:
				return new QueryConfigResponse(buf);
				
			case 4:
				return new SetTimeResponse(buf);
				
			case 7:
				return new ReadDataResponse(buf);
	
			default:
				return null;
		}
	}
	
	public static BaseResponse unpack(byte[] buf) {
		BaseResponse response = getResponse(buf);
		if(response != null) {
			response.unpack();
		}
		
		return response;
	}
}
