package com.cloud.emr.pattern.state;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.cloud.emr.service.TransportService;
import com.cloud.emr.util.DateUtil;
import com.cloud.emr.util.Log;
import com.cloud.emr.util.NetworkUtil;

/**
 * 
 * 项目名称：EMR   
 * 类名称：数据转发
 * 类描述：暂无 
 * 创建人：lw   
 * 创建时间：2014-2-27 下午04:30:33   
 * 修改人：lw   
 * 修改时间：2014-3-25 下午04:30:33   
 * 修改备注： 
 * 遗留问题：运动和心电转发过。
 * @version
 */
public class SendState extends State {
	private TransportService transportService = null;
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void handle(Context context) {
		this.transportService = context.getTransportService();
		Log.getLogger().i("ToAPP 开始发送"+context.getData().getDataType()+"数据...");
		List<HashMap<String,String>> uri_list = context.getUri_list();
		Data data = context.getData();
		//update collectDate
		data.setCollectDate(DateUtil.getCurrentTime());
		String params = JSONObject.fromObject(data).toString();
		Map map = new HashMap();
		map.put("data", params);
		for(int i=0;i<uri_list.size();i++){
			HashMap<String,String> uri_map = uri_list.get(i);
			String url = uri_map.get("locationUrl")!=null?uri_map.get("locationUrl").toString():"";
			String locationId = uri_map.get("locationId").toString()!=null?uri_map.get("locationId").toString():"";
			if("".equals(url)||"".equals(locationId)){
				Log.getLogger().e("post data error requare param has null value : url-"+url+" locationId-"+locationId);
				continue;
			}
			boolean ret = NetworkUtil.postData(url,map);
			Log.getLogger().i("ToAPP "+context.getData().getDataType()+" 发送结果："+(ret?"成功":"失败"));
			//目前packdata支持心电、睡眠、血糖、血压、日常运动、心功能运动、体重7个种数据，不支持运动等级。
			if(!ret){
				String insert_sql = "INSERT INTO packdata(appType,dataType,collectDate,phone,dataValue,location_id,in_time) VALUES('%s','%s','%s','%s','%s','%s','%s')";
				try {
					boolean bool =this.transportService.insertPackdata(String.format(insert_sql, 
							data.getAppType(),data.getDataType(),data.getCollectDate(),data.getPhone(),data.getDataValue(),locationId,DateUtil.getCurrentTime()));
					if(!bool){
						Log.getLogger().e("inert database error , with data:"+context.getData().toString());
					}
				} catch (Exception e) {
					Log.getLogger().e("inert database error :"+e.getMessage());
				}
			}else{//if send data success 由于网络原因，补发已经缓存的数据
				if(isNeedSend()){
					autoSendPack();
					Log.getLogger().i("*C*3*P*0*\r\n"+this.transportService.getC3P0Info());
				}
			}
		}
		//为了告诉网关已经接收数据，否则网关继续发送数据。
		context.getResponse().setStatus(200);
		Log.getLogger().i("\r\n^^^^^^^^^^^^^^^^^^^^接口调用结束^^^^^^^^^^^^^^^^^^^^\r\n");
		
	}
	// auto send data max 60 record
	public synchronized void autoSendPack(){
		List<HashMap<String,String>> list = this.transportService.queryBakPackData();
		int i=0;
		for(HashMap<String,String> map:list){
			map.put("collectDate", DateUtil.getCurrentTime());
			this.transportService.sendPackdata(map);
			Log.getLogger().i("ToAPP autoSend data! send "+i+"/"+list.size());
			i++;
		}
	}
	// check weather to send
	public boolean isNeedSend(){
		boolean bool = false;
		long l = System.currentTimeMillis();
		Log.getLogger().i("isNeedSend l = "+l+" mould 7 = "+(l % 7));
		if (l % 7 == 0)
			bool = true;
		return bool;
	}
}
