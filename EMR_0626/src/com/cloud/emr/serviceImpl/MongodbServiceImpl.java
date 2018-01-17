package com.cloud.emr.serviceImpl;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.emr.base.Domain.HealthData;
import com.cloud.emr.dao.HealthDataDao;
import com.cloud.emr.service.MongodbService;
@Service
public class MongodbServiceImpl implements MongodbService {

	@Autowired
	private HealthDataDao healthDataDao;
	@Override
	public String insertOne(JSONObject... js) {
		for (int i =0,n=js.length;i<n;i++) {
			JSONObject object=js[i];
			System.out.println(object.toString());
			HealthData data = (HealthData) JSONObject.toBean(object, HealthData.class);  
			System.out.println(data.toString());
			 healthDataDao.saveOne(data);
		}
		return "";
	}
}
