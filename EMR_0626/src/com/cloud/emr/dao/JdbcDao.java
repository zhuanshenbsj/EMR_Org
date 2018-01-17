package com.cloud.emr.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.support.rowset.SqlRowSet;
/**
 * 
 * 项目名称：EMR   
 * 类名称：JdbcDao   
 * 类描述：暂无 
 * 创建人：lw   
 * 创建时间：2014-1-26 下午06:13:46   
 * 修改人：lw   
 * 修改时间：2014-1-26 下午06:13:46   
 * 修改备注： 
 * @version
 */
public interface JdbcDao {
	/**
	 * get monitor c3p0 pool info
	 */
	public String monitor();
	/**
	 * execute the sql
	 * @param sql
	 */
	public void execute(final String sql);
	/**
	 * delete the sql
	 * @param sql
	 * @return
	 */
	public int delete(final String sql);
	/**
	 * add the sql
	 * @param sql
	 * @return
	 */
	public int add(final String sql);
	/**
	 * update the sql
	 * @param sql
	 * @return
	 */
	public int update(final String sql);
	/**
	 * batch execute sql
	 * @param sql
	 * @return
	 */
	public int[] batchUpdate(final String sql[]);
	/**
	 * query for list by sql
	 * @param sql
	 * @return
	 */
	public  List<?> queryForList(String sql);
	/**
	 * query for list by sql and elementType
	 * @param <T>
	 * @param sql
	 * @param elementType
	 * @return
	 */
	public <T> List<T> queryForList(String sql, Class<T> elementType);
	/**
	 * query for rowset by sql
	 * @param sql
	 * @return
	 */
	public SqlRowSet queryForRowSet(String sql);
	/**
	 * simple query
	 * @param sql
	 * @return int value
	 */
	public int queryForInt(String sql);
	/**
	 * simple query
	 * @param sql
	 * @return long value
	 */
	public long queryForLong(String sql);
	/**
	 * simple query
	 * @param sql
	 * @return map object
	 */
	@SuppressWarnings("rawtypes")
	public Map queryForMap(String sql);
	/**
	 * query for object
	 * @param sql
	 * @param requiredType
	 * @return
	 */
	public Object queryForObject(String sql, Class<?> requiredType);
	/**
	 * get count
	 * @param sql
	 * @return
	 */
	public int getCount(String sql);
	/**
	 * execute update 
	 * @param sql
	 * @return true if success
	 */
	public boolean executeUpdate(String sql);
	/**
	 * execute delete
	 * @param sql
	 * @return
	 */
	public int executeDelete(String sql);
	 /**
	 * get map data with key is column'name value is column's value
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, String>> getData(String sql);
	/**
	 * get page data
	 * 
	 * @param sql
	 * 
	 * @param pageno 
	 * 					page number
	 * @param pagesize
	 * 					page size
	 * @return
	 */
	public List<HashMap<String,String>> getScollData(String sql,int pageno,int pagesize);
	/**
	 * get obs data
	 * @param appType
	 * @param conceptDescribe
	 * @param startTime
	 * @param endTime
	 * @param isMq
	 * @return
	 */
	public List<HashMap<String,String>> getObsData(String appType,String conceptDescribe,String startTime,String endTime,String isMq);
	/**
	 * just for hospital
	 * @param sql
	 * @param changeSecond
	 * @return
	 */
	public String getJson(String sql);
}
