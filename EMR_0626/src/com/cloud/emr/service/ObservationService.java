package com.cloud.emr.service;

import java.util.HashMap;
import java.util.List;

import com.cloud.emr.base.Domain.Concept;
/**
 * 
 * 项目名称：EMR   
 * 类名称：ObservationService   
 * 类描述：Observation业务层 
 * 创建人：lw   
 * 创建时间：2013-11-19 上午11:11:21   
 * 修改人：lw   
 * 修改时间：2013-11-19 上午11:11:21   
 * 修改备注： 
 * @version
 */
public interface ObservationService {
	/**
	 * 通用的数据插入接口
	 * @param uniqueField phone number
	 * @param dateTime	measureTime
	 * @param businessType from concept table's conceptDescribe filed
	 * @param appType app type 
	 * @param param JSONObject string
	 * @return
	 */
	public boolean insertOrUpdateData(String uniqueField,String dateTime,String businessType,String appType,String param);
	/**
	 * 通用的数据插入接口
	 * @param uniqueField phone number
	 * @param dateTime	measureTime
	 * @param businessType from concept table's conceptDescribe filed
	 * @param appType app type 
	 * @param param JSONObject string
	 * @param receiveDateTime when the date be collected
	 * @return
	 */
	public boolean insertOrUpdateData(String uniqueField,String dateTime,String businessType,String appType,String param,String collectDate);
    /**
	 * 体重信息插入
	 * @param encounterDatetime 记录时间
	 * @param idcard 身份证号
	 * @param weight 体重
	 * @param appType 系统类别
	 * @return
	 */
}