package com.cloud.emr.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.emr.dao.JdbcDao;
import com.cloud.emr.service.TransportService;
import com.cloud.emr.util.Log;
import com.cloud.emr.util.NetworkUtil;

@Service
public class TransportServiceImpl implements TransportService {
	@Autowired
	private JdbcDao jdbcDao;

	@Override
	public List<HashMap<String, String>> getSendUrl(String phone, String appType) {
		String sql = "SELECT pl.plid,l.locationUrl,l.locationId FROM patient p,patient_location pl,location l "
				+ "WHERE p.patientId = pl.patientId AND l.locationId = pl.locationId AND p.phone = '%s' AND p.appType = '%s' ";
		return this.jdbcDao.getData(String.format(sql, phone, appType));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void dealDataValue(Map param) {
		String dataValue = param.get("dataValue").toString();
		JSONArray ja = JSONArray.fromObject(dataValue);
		param.put("dataValue", ja);
	}

	@Override
	public List<HashMap<String, String>> queryBakPackData() {
		String sql = "SELECT p.pack_data_id,p.appType,p.dataType,p.collectDate,p.phone,p.dataValue,p.in_time,l.locationUrl FROM packdata p JOIN location l ON p.location_id = l.locationId  LIMIT 60 ";
		return this.jdbcDao.getData(sql);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void sendPackdata(Map param) {
		dealDataValue(param);
		String url = param.get("locationUrl").toString();
		param.remove("locationUrl");
		String packdata_id = param.get("pack_data_id").toString();
		param.remove("packdata_id");
		Map map = new HashMap();
		map.put("data", JSONObject.fromObject(param).toString());
		boolean flag = NetworkUtil.postData(url, map);
		Log.getLogger().i("ToAPP auto send 发送结果：" + (flag ? "成功" : "失败"));
		if (flag) {
			int num = this.jdbcDao
					.delete("delete from packdata where pack_data_id = "
							+ packdata_id);
			if (num != 0) {
				Log.getLogger().i("delete packdata - success:" + param);
			} else {
				Log.getLogger().e("delete packdata - failure:" + param);
			}
		} else {
			Log.getLogger().e("toAPP-failure:" + param);
		}
	}
	@Override
	public String getC3P0Info(){
		return this.jdbcDao.monitor();
	}
	
	@Override
	public boolean insertPackdata(String insert_sql){
		return this.jdbcDao.add(insert_sql)>0;
	}
}
