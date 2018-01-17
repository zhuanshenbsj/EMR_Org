package com.cloud.emr.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.cloud.emr.dao.JdbcDao;
import com.mchange.v2.c3p0.PooledDataSource;
/**
 * 
* 项目名称：EMR   
* 类名称：JdbcDaoImpl   
* 类描述： JdbcDao接口的实现
* 创建人：lw   
* 创建时间：2013-11-19 上午10:39:37   
* 修改人：lw   
* 修改时间：2013-11-19 上午10:39:37   
* 修改备注： 
* @version
 */
@Repository
public class JdbcDaoImpl implements JdbcDao{
	@Autowired
	JdbcTemplate jdbcTemplate;
	@Override
	public String monitor(){
		StringBuffer re_value = new StringBuffer();
		re_value.append("monitor c3p0 pool ");
		DataSource ds = this.jdbcTemplate.getDataSource();
		// make sure it's a c3p0 PooledDataSource
		if (ds instanceof PooledDataSource) {
			try {
				PooledDataSource pds = (PooledDataSource) ds;
				re_value.append("\r\nnum_connections: ")
						.append(pds.getNumConnectionsDefaultUser())
						.append("\r\nnum_busy_connections: ")
						.append(pds.getNumBusyConnectionsDefaultUser())
						.append("\r\nnum_idle_connections: ")
						.append(pds.getNumIdleConnectionsDefaultUser());
			} catch (SQLException e) {
				re_value.append("\r\n"+e.getMessage());
			}
		} else {
			re_value.append("\r\nNot a c3p0 PooledDataSource!");
		}
		return re_value.toString();
	}
	@Override
	public  void execute(final String sql){
		jdbcTemplate.execute(sql);
	}
	@Override
	public int delete(String sql) {
		return jdbcTemplate.update(sql);
	}

	@Override
	public int add(String sql) {
		final String sqlSave = sql.toString();
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sqlSave, Statement.RETURN_GENERATED_KEYS);
				return ps;
			}
		}, keyHolder);
		int code = keyHolder.getKey().intValue();
		return code;
	}
	@Override
	public int update(String sql) {
		return jdbcTemplate.update(sql);
	}
	@Override
	public int[] batchUpdate(String[] sql) {
		return jdbcTemplate.batchUpdate(sql);
	}
	@Override
	public List<?> queryForList(String sql) {
		
		return jdbcTemplate.queryForList(sql);
	}
	@Override
	public <T> List<T> queryForList(String sql, Class<T> elementType) {
		return jdbcTemplate.queryForList(sql, elementType);
	}
	@Override
	public SqlRowSet queryForRowSet(String sql) {
		return jdbcTemplate.queryForRowSet(sql);
	}
	@Override
	public int queryForInt(String sql) {
		int re_int = 0;
		try {
			re_int = jdbcTemplate.queryForInt(sql);
		} catch (EmptyResultDataAccessException e) {
			re_int = 0;
		} catch (IncorrectResultSizeDataAccessException e) {
			re_int = -1;
		}
		return re_int;
	}
	@Override
	public long queryForLong(String sql) {
		return jdbcTemplate.queryForLong(sql);
	}
	@SuppressWarnings("rawtypes")
	@Override
	public Map queryForMap(String sql) {
		return jdbcTemplate.queryForMap(sql);
	}
	@Override
	public Object queryForObject(String sql, Class<?> requiredType) {
		Object obj = null;
		try {
			obj = jdbcTemplate.queryForObject(sql, requiredType);
		} catch (DataAccessException e) {
			obj = null;
		}
		return obj;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public int getCount(String sql) {
		return jdbcTemplate.query(sql, new ResultSetExtractor(){
			@Override
			public Integer extractData(ResultSet rs)
					throws SQLException, DataAccessException {
				if(rs.next()){
	        		return Integer.parseInt(rs.getString(1));
	            }else{
	            	return 0;
	            }
			}});
	}
	@Deprecated
	@Override
	public boolean executeUpdate(String sql) {
		boolean ret = true;
		try {
			jdbcTemplate.execute(sql);
		} catch (Exception e) {
			e.printStackTrace();
			ret = false;
		} 
		return ret;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public int executeDelete(final String sql) {
		   Integer result = 0;
		   result = jdbcTemplate.execute(new StatementCallback(){
			@Override
			public Object doInStatement(Statement statement)
					throws SQLException, DataAccessException {
				return statement.executeUpdate(sql);
			}
			   
		   });
		   return result;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<HashMap<String, String>> getData(final String sql)
	{
        return jdbcTemplate.execute(new StatementCallback(){
			@Override
			public List doInStatement(Statement statement)
					throws SQLException, DataAccessException {
				List<HashMap<String,String>> result = new ArrayList<HashMap<String,String>>();
				ResultSet rs = statement.executeQuery(sql);
				ResultSetMetaData rsmd = rs.getMetaData();
	        	while(rs.next())
	        	{
	        		HashMap<String,String> map = new HashMap<String,String>();
	        		for(int i=0;i<rsmd.getColumnCount();i++)
	        		{
	        			map.put(rsmd.getColumnLabel(i+1), rs.getString(i+1));
	        		}
	        		result.add(map);
	        	}
	        	return result;
			}
        	
        });
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<HashMap<String,String>> getScollData(final String sql,final int pageno,final int pagesize){
		return jdbcTemplate.execute(new ConnectionCallback(){
			@Override
			public List doInConnection(Connection conn)
					throws SQLException, DataAccessException {
				PreparedStatement  pstat = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
				List<HashMap<String,String>> result = new ArrayList<HashMap<String,String>>();
	            //最大查询的记录条数
	            pstat.setMaxRows(pageno*pagesize);
	            ResultSet rs = pstat.executeQuery();
	            //将游标移动到第一条记录
	            rs.first();
	            //游标移动到要输出的第一条记录
	            rs.relative((pageno-1)*pagesize-1);
	            ResultSetMetaData rsmd = rs.getMetaData();
	            while(rs.next())
	            {
	        	  HashMap<String,String> map = new HashMap<String,String>();
	        	  for(int i=0;i<rsmd.getColumnCount();i++)
	        	  {
	        		  map.put(rsmd.getColumnLabel(i+1), rs.getString(i+1));
	        	  }
	        	  result.add(map);
	            }
		         return result; 
		}});
	}
	@Override
	public List<HashMap<String,String>> getObsData(String appType,String conceptDescribe,String startTime,String endTime,String isMq){
		String condition = "";
		if(null!=startTime&&null!=endTime){
			condition = "and date(C.obsDatetime) >= '"+startTime+"' and date(C.obsDatetime) <= '"+endTime+"'";
		}
		String sql = "select B.encounterId,A.patientId,A.idcard,A.phone,A.email,A.name,C.obsDatetime,C.value,D.conceptName from patient A,#enc# B,#obs# C,concept D where A.patientId = B.patientId and B.encounterId = C.encounterId and C.conceptId = D.conceptId and D.conceptDescribe = '"+conceptDescribe+"' "+condition+" and C.encounterType ='"+appType+"'";
		if(null==isMq){
			sql = sql.replace("#enc#", "encounter");
			sql = sql.replace("#obs#", "observation");
		}else{
			sql = sql.replace("#enc#", "mqencounter");
			sql = sql.replace("#obs#", "mqobservation");
		}
		List<HashMap<String,String>> list = null;
		try {
			list = this.getData(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String,HashMap<String,String>> rowData = new HashMap<String,HashMap<String,String>>();
		for(HashMap<String,String> temp : list){
			HashMap<String,String> data = rowData.get(temp.get("encounterId").trim());
			if(null==data){
				data =  temp;
				rowData.put(temp.get("encounterId").trim(), data);
			}
			data.put(temp.get("conceptName").trim(), temp.get("value"));
		}
		List<HashMap<String,String>> result = new ArrayList<HashMap<String,String>>();
        for(HashMap<String,String> map : rowData.values()){
        	result.add(map);
        }
        rowData = null;
        Collections.sort(result,new Comparator<HashMap<String,String>>() {
			@Override
			public int compare(HashMap<String,String> runOne, HashMap<String,String> runTwo) {
				return runTwo.get("obsDatetime").compareTo(runOne.get("obsDatetime"));
			}
		});
		return result;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String getJson(final String sql){
		try {
			return jdbcTemplate.execute(new StatementCallback(){
				@Override
				public String doInStatement(Statement statement)throws SQLException, DataAccessException {
					StringBuffer result=new StringBuffer("[");
					ResultSet rs = statement.executeQuery(sql);
			    	while(rs.next())
			    	{
			    		result.append("[");
			        	//difference 8 hours
			        	result.append(rs.getTimestamp(1).getTime()+1000*60*60*8);
						result.append(",");
						result.append(rs.getInt(2)).append("]").append(",");
			    	}
			    	if(result.toString().endsWith(","))
			    		result.deleteCharAt(result.lastIndexOf(","));
			        result.append("]");
			        return result.toString();
				}
			});
		} catch (DataAccessException e) {
			return "日常数据查询异常!异常信息:\r\n"+e.getMessage();
		}
	}
}
