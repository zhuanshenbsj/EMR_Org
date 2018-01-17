package com.cloud.emr.daoImpl;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.cloud.emr.base.Domain.HealthData;
import com.cloud.emr.dao.HealthDataDao;
import com.cloud.emr.dao.MongodbBaseDao;
@Repository
public class HealthDataDaoImpl  extends MongodbBaseDao<HealthData> implements HealthDataDao{

	@Override
	protected Class getEntityClass() {
		return HealthData.class;
	}
	@Autowired
	@Override
	protected void setMongoTemplate(@Qualifier("mongoTemplate")MongoTemplate mongoTemplate) {
		super.mongoTemplate=mongoTemplate;
	}
	@Override
	public void saveOne(HealthData data) {
		this.save(data);
	}
}
