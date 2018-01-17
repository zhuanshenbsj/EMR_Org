package com.cloud.emr.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface TransportService {
	
	/**
	 * get patient's url
	 * @param phone
	 * @param appType
	 * @return
	 */
	public List<HashMap<String, String>> getSendUrl(String phone, String appType);

	/**
	 * send param to the given url
	 * @param url
	 * @param param
	 */
	@SuppressWarnings("rawtypes")
	public void sendPackdata(Map param);
	
	
	/**
	 * query data which needs sends
	 * @param param
	 * @return
	 */
	public List<HashMap<String, String>> queryBakPackData();
	
	/**
	 * get C3P0 pool state
	 * @return
	 */
	public String getC3P0Info();
	
	/**
	 * insert data into table packdata
	 * @param insert_sql
	 * @return
	 */
	public boolean insertPackdata(String insert_sql);
}
