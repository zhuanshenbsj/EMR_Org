package com.cloud.emr.pattern.state;

import java.util.HashMap;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.cloud.emr.util.JsonUtil;
import com.cloud.emr.util.Log;

/**
 * 
 * 项目名称：EMR   
 * 类名称：ReceiveState   
 * 类描述：暂无 
 * 创建人：lw   
 * 创建时间：2014-2-27 下午04:30:33   
 * 修改人：lw   
 * 修改时间：2014-2-27 下午04:30:33   
 * 修改备注： 
 * @version
 */
public class ReceiveState extends State {
	@Override
	public void handle(Context context) {
		Log.getLogger().i("now is in receive state");
		String params = context.getRequest().getParameter("data");
		JSONObject jo = JSONObject.fromObject(params);
		String phone = JsonUtil.getJsonParamterString(jo, "phone");
		String appType = JsonUtil.getJsonParamterString(jo, "appType");
		String dataType = JsonUtil.getJsonParamterString(jo, "dataType");
		List<HashMap<String, String>> url = context.getTransportService().getSendUrl(phone,appType);
		if(url==null||url.size()==0){
			Log.getLogger().e(appType+"'s patient with phone "+phone+"has no URL to send or patient not exists! package'type is :"+dataType);
			Log.getLogger().i("\r\n^^^^^^^^^^^^^^^^^^^^接口调用结束^^^^^^^^^^^^^^^^^^^^\r\n");
			return;
		}else{
			context.setUri_list(url);
			Data data = new Data();
			data.setAppType(appType);
			data.setDataType(dataType);
			data.setPhone(phone);
			data.setCollectDate(JsonUtil.getJsonParamterString(jo, "collectDate"));
			data.setDataValue(JsonUtil.getJsonParamterArray(jo, "dataValue"));
			Log.getLogger().i("receive data : "+data.toString());
			context.setData(data);
			context.setState(new SaveState());
			context.request();
		}
	}

}
