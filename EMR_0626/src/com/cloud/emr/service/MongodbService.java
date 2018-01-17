package com.cloud.emr.service;

import net.sf.json.JSONObject;

public interface MongodbService {
	public String insertOne(JSONObject... js);
}
