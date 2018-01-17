package com.cloud.emr.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class C3P0Util {
	private static Logger log = Logger.getLogger(C3P0Util.class);
	private static DataSource ds = null;
	static {
		// 默认读取classpath下的c3p0-config.xml
		ds = new ComboPooledDataSource();
		// 连接abc数据库
		// ds = new ComboPooledDataSource("abc");

	}
   /**
	* 获取一个数据库连接
	* @return Connection
	*/
	public static Connection getConnection(){
		
		try {
			return ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("从c3p0连接池获取数据库连接失败！");
		}
		return null;
	}

public static int getCount(String sql)
   {
		Connection conn=null;
        Statement st=null;
        ResultSet rs=null;int i=0;
        try{
        	conn=getConnection();
        	st=conn.createStatement();
        	rs=st.executeQuery(sql);
        	if(rs.next()){
        		i=Integer.parseInt(rs.getString(1));
            }
        } catch (Exception e) {
        	log.error(e.getMessage());
			e.printStackTrace();
		}finally{
        	releaseResource(conn, st, rs);
        }
        return i;
   }

	public static boolean executeUpdate(String sql){
		Connection conn = null;
		Statement st = null;
		boolean ret = true;
		try {
			conn = getConnection();
			st = conn.createStatement();
			st.executeUpdate(sql);
			System.out.println(sql);
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			ret = false;
		} finally {
			releaseResource(conn, st, null);
		}
		return ret;
	}
	/**
	 * 可插入任意个数 value参数,但sql中的问号个数务必于value个数相同
	 * @param sql
	 * @param value
	 * @return
	 * @throws SQLException
	 * boolean
	 */
	public static boolean insertOrUpdateData(String sql,String...value){
		log.info(sql+Arrays.toString(value));
		Connection conn = null;
		PreparedStatement prst = null;
		boolean ret = false;
		int num = 0;
		try {
			conn = getConnection();
			prst = conn.prepareStatement(sql);
			for(int i=0;i<value.length;i++){
				prst.setString(i+1, value[i]);
			}
			num = prst.executeUpdate();
			if(num>0){
				ret = true;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			ret = false;
		} finally {
			releaseResource(conn, prst, null);
		}
		return ret;
	}
   /**
    * 执行删除操作，并返回影响的行数
    * @param sql
    * @return
    * @throws SQLException
    */
   public static int executeDelete(String sql) throws SQLException{
	   Connection conn=null;
	   Statement st=null;
	   int result = 0;
	   try{
		   conn=getConnection();
		   st=conn.createStatement();
		   result = st.executeUpdate(sql);
		   System.out.println(sql);
	   } catch (Exception e) {
		   log.error(e.getMessage());
		   e.printStackTrace();
	   }finally{
		   releaseResource(conn, st, null);
	   }
	   return result;
   }
   
   /**
	 * JDBC获取数据，结果组织成HashMap形式，key为列名，value为数据
	 * 一条HashMap对应于查询到的一条数据
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public static List<HashMap<String, String>> getData(String sql)
	{
		Connection conn=null;
        Statement st=null;
        ResultSet rs=null;
        List<HashMap<String,String>> result = new ArrayList<HashMap<String,String>>();
        try{
        	conn=getConnection();
        	st=conn.createStatement();
        	rs=st.executeQuery(sql);
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
        }catch (Exception e) {
        	log.error(e.getMessage());
			e.printStackTrace();
		}finally{
			releaseResource(conn, st, rs);
        }
        return result;
	}
	
	public static List<HashMap<String,String>> getScollData(String sql,int pageno,int pagesize)
	{
		Connection conn=null;
		PreparedStatement pstat=null;
        ResultSet rs=null;
        
        List<HashMap<String,String>> result = new ArrayList<HashMap<String,String>>();
        try {
       //   conn.prepareStatement(sql,游标类型,能否更新记录);
//             游标类型：
//              ResultSet.TYPE_FORWORD_ONLY:只进游标
//              ResultSet.TYPE_SCROLL_INSENSITIVE:可滚动。但是不受其他用户对数据库更改的影响。
//              ResultSet.TYPE_SCROLL_SENSITIVE:可滚动。当其他用户更改数据库时这个记录也会改变。
//             能否更新记录：
//              ResultSet.CONCUR_READ_ONLY,只读
//              ResultSet.CONCUR_UPDATABLE,可更新
          conn=getConnection();
          pstat = conn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
          //最大查询的记录条数
          pstat.setMaxRows(pageno*pagesize);
          rs = pstat.executeQuery();
          //将游标移动到第一条记录
          rs.first();
       //   游标移动到要输出的第一条记录
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
        }
        catch (Exception e) {
          e.printStackTrace();
        }finally{
        	releaseResource(conn, pstat, rs);
        }
        return result;
	}
	/**
	 * 释放数据库资源
	 * @param Connection conn
	 * @param PreparedStatement pstmt
	 * @param ResultSet rs
	 */
	public static void releaseResource(Connection conn,PreparedStatement pstmt,ResultSet rs){
		try {//关闭顺序：rs,pstmt,conn
			if (rs != null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
			if (conn != null)
				conn.close();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	/**
	 * 重载 释放数据库资源
	 * @param Connection conn
	 * @param Statement stmt
	 * @param ResultSet rs
	 */
	public static void releaseResource(Connection conn, Statement stmt,ResultSet rs) {
		try {//关闭顺序：rs,stmt,conn
			if (rs != null)
				rs.close();
			if (stmt != null)
				stmt.close();
			if (conn != null)
				conn.close();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		List list = null;
		try {
			getScollData("select * from bloodpressure", 1, 1000);
			System.out.println(getConnection());
//			list = getRestFulData("tx", "bloodpressure", "412724198703156495", "2013-01-01", "2013-08-08", 1, 30);
//			list = getRestFulData("ws", "runmessage", "152801198708223332", "2000-01-01", "2099-08-08", 1, 0);
//			list = getData("select A.patientId,A.idcard,A.name,A.phone,A.email,B.encounterDatetime,B.encounterType,C.value,D.conceptName from patient A,encounter B,observation C,concept D where A.patientId = B.patientId and B.encounterId = C.encounterId and C.conceptId = D.conceptId and A.appType ='ws' and D.conceptDescribe  = 'runmessage' and A.idcard = '' and B.encounterDatetime >= '2010-01-01' and B.encounterDatetime <='2013-08-08'");
		} catch (Exception e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
//		System.out.println((end-start)+"@"+list.size());
	}
	
	/**
	 * 判断 map 是否包含 key
	 * @param map
	 * @param key
	 * @return 包含返回value ，不包含返回 null
	 */
	public static Object getMapElement(Map map,String key){
		if (map!=null) {
			for (Iterator ite = map.entrySet().iterator(); ite.hasNext();) {
				Map.Entry entry = (Map.Entry) ite.next();
				if ((key.trim()).equals(entry.getKey()))
					return entry.getValue();
			}
		}
		return null;
	}
}
