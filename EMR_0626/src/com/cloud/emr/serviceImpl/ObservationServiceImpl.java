package com.cloud.emr.serviceImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.exolab.castor.types.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cloud.emr.base.Domain.Patient;
import com.cloud.emr.dao.JdbcDao;
import com.cloud.emr.service.ObservationService;
import com.cloud.emr.util.Constants;
import com.cloud.emr.util.DateUtil;
import com.cloud.emr.util.MongoDBHelper;
import com.cloud.emr.util.PropertiesReader;
/**
 * 
 * 项目名称：EMR   
 * 类名称：ObservationServiceImpl   
 * 类描述：暂无 
 * 创建人：lw   
 * 创建时间：2014-1-26 下午06:10:26   
 * 修改人：lw   
 * 修改时间：2014-1-26 下午06:10:26   
 * 修改备注： 
 * http tool||rest clint for firefox call
 * http://localhost:8080/EMR_0626/service/businessDataInsert/13910828743/20170701144351/HeartFunction/WSXHH.json?param={"hfStartTime":"08:00","hfStartHeartRate":"79","hfEndTime":"21:00","hfEndHeartRate":"120","hfSteps":"120"}
 * http tool需设置：Headers,选择custom Header。name:Content-Type,value:application/x-www-form-urlencoded;charset=UTF-8
 * @version
 */
@Service
public class ObservationServiceImpl implements ObservationService {
	private static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ObservationServiceImpl.class);
	@SuppressWarnings({ "rawtypes", "unchecked" })


	@Autowired
	private JdbcDao jdbcDao;
	
	@SuppressWarnings("rawtypes")
	public boolean insertOrUpdateData(String uniqueField,String dateTime,String businessType,String appType,String param){
		//find patientId
		int patientId = this.queryPatientByPhone(uniqueField,appType);
		if(0 == patientId){
			log.debug("patient not found,param is :"+"uniqueField-"+uniqueField+" appType-"+appType);
			return false;//if not exists the patient
		}else{
			String concept_sql = "SELECT conceptId,conceptName FROM concept WHERE conceptDescribe = '"+businessType+"'";
			List list = this.jdbcDao.getData(concept_sql);
			JSONObject jo = JSONObject.fromObject(param);
			String[] sql = new String[list==null?0:list.size()];
			String check_sql = "SELECT COUNT(1) FROM observation WHERE conceptId = '%s' AND patientId = '%s' AND obsDatetime = '%s' ";
			String insert_sql = "INSERT into `observation` (`obsDatetime`, `value`, `conceptId`, `patientId`,`collectDate`,`receiveDateTime`) values('%s','%s','%s','%s','%s','%s') ";
			String update_sql = "UPDATE observation set value = '%s' WHERE conceptId = '%s' AND patientId = '%s' AND obsDatetime = '%s' ";
			for(int i=0;i<list.size();i++){
				Map map = (Map)list.get(i);
				int count = this.jdbcDao.queryForInt(String.format(check_sql, map.get("conceptId").toString(),patientId,dateTime));
				if(0==count){//not exists record
					sql[i]=String.format(insert_sql,dateTime,jo.getString(map.get("conceptName").toString()),map.get("conceptId").toString(),patientId,DateUtil.getCurrentTime(),DateUtil.getCurrentTime());
				}else{
					log.info("patient "+patientId+" exists "+map.get("conceptName").toString()+" record execute update" );
					sql[i]=String.format(update_sql, jo.getString(map.get("conceptName").toString()),map.get("conceptId").toString(),patientId,dateTime);
				}
			}
			if(sql.length>0){
				try {
					this.jdbcDao.batchUpdate(sql);
//					int a=1/0;
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					log.debug("batchUpdate data error !\r\n"+e.getMessage());
					//模拟异常信息
//					throw e;
					return false;
				}
			}
			else{
				log.debug("can't find concept data error !" );
				return false;
			}
			
		}
		
	}
	@Override
	@SuppressWarnings("rawtypes")
	public boolean insertOrUpdateData(String uniqueField,String dateTime,String businessType,String appType,String param,String collectDate){
		//find patientId
		int patientId = this.queryPatientByPhone(uniqueField,appType);
		if(0 == patientId){
			log.debug("patient not found,param is :"+"uniqueField-"+uniqueField+" appType-"+appType);
			return false;//if not exists the patient
		}else{
			String concept_sql = "SELECT conceptId,conceptName FROM concept WHERE conceptDescribe = '"+businessType+"'";
			List list = this.jdbcDao.getData(concept_sql);
			JSONObject jo = JSONObject.fromObject(param);
			String[] sql = new String[list==null?0:list.size()];
			String check_sql = "SELECT COUNT(1) FROM observation WHERE conceptId = '%s' AND patientId = '%s' AND obsDatetime = '%s' ";
			String insert_sql = "INSERT into `observation` (`obsDatetime`, `value`, `conceptId`, `patientId`,`collectDate`,`receiveDateTime`) values('%s','%s','%s','%s','%s','%s') ";
			String update_sql = "UPDATE observation set value = '%s',collectDate = '%s',receiveDateTime = '%s' WHERE conceptId = '%s' AND patientId = '%s' AND obsDatetime = '%s' ";
			for(int i=0;i<list.size();i++){
				Map map = (Map)list.get(i);
				int count = this.jdbcDao.queryForInt(String.format(check_sql, map.get("conceptId").toString(),patientId,dateTime));
				if(0==count){//not exists record
					sql[i]=String.format(insert_sql,dateTime,jo.getString(map.get("conceptName").toString()),map.get("conceptId").toString(),patientId,collectDate,DateUtil.getCurrentTime());
				}else{
					log.info("patient "+patientId+" exists "+map.get("conceptName").toString()+" record execute update" );
					sql[i]=String.format(update_sql, jo.getString(map.get("conceptName").toString()),collectDate,DateUtil.getCurrentTime(),map.get("conceptId").toString(),patientId,dateTime);
				}
			}
			if(sql.length>0){
				try {
					int[] batchUpdate = this.jdbcDao.batchUpdate(sql);

					//用除0 异常 模拟 实际可能出现的异常
//					int a=1/0;
					return true;
				} catch (Exception e) {
					e.printStackTrace();
					log.debug("batchUpdate data error !\r\n"+e.getMessage());
//					throw e;
					return false;
				}
			}
			else{
				log.debug("can't find concept data error !" );
				return false;
			}
			
		}
		
	}
	/**
	 * find the patient by unique field
	 * @param uniqueField
	 * @param appType
	 * @return
	 */
	public int queryPatientByPhone(String phone,String appType){
		String query_sql = "select patientId from patient p where 1=1 and p.appType = '"+appType+"' "+" and p.phone = '"+phone+"' ";
		return this.jdbcDao.queryForInt(query_sql);
	}
	
}
