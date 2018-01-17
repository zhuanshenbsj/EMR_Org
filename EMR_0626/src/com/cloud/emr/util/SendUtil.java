package com.cloud.emr.util;

import java.util.HashMap;
import java.util.Map;

public class SendUtil {
	
	// check weather to send
	public static boolean isNeedSend(){
		return System.currentTimeMillis()%7==0;
	}
	
	public static boolean sendData(String appType,String config,String data){
		boolean bool = false;
		if(config!=null){
			Map<String,String> map = new HashMap<String,String>();
			map.put("data", data);
			String[] urls = config.split(";");
			for (int i = 0; i < urls.length; i++) {
				boolean ret = NetworkUtil.postData(urls[i],map);
				bool = ret;
				Log.getLogger().i("ToAPP "+appType+" 发送结果："+(ret?"成功":"失败"));
			}
		}
		return bool;
	}
}
