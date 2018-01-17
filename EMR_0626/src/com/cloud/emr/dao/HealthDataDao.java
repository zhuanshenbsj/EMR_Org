package com.cloud.emr.dao;

import com.cloud.emr.base.Domain.HealthData;

import net.sf.json.JSONObject;

public interface HealthDataDao {

	public void saveOne(HealthData data);
}
