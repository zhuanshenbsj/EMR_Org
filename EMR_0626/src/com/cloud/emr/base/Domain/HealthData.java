package com.cloud.emr.base.Domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;

import com.google.gson.JsonArray;


@Document(collection="C_HealthData")  
public class HealthData  implements Serializable{
	@Override
	public String toString() {
		return "HealthData [appType=" + appType + ", dataType=" + dataType
				+ ", phone=" + phone + ", collectDate=" + collectDate
				+ ", dataValue=" + dataValue + ", sendFlag=" + sendFlag
				+ ", receiveTime=" + receiveTime + "]";
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String appType;
	private String dataType;
	private String phone;
	private String collectDate;
	private Map<String,String>[] dataValue;

	private String sendFlag;
	private String receiveTime;
	public String getAppType() {
		return appType;
	}
	public void setAppType(String appType) {
		this.appType = appType;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCollectDate() {
		return collectDate;
	}
	public void setCollectDate(String collectDate) {
		this.collectDate = collectDate;
	}
	public Map<String, String>[] getDataValue() {
		return dataValue;
	}
	public void setDataValue(Map<String, String>[] dataValue) {
		this.dataValue = dataValue;
	}
	public String getSendFlag() {
		return sendFlag;
	}
	public void setSendFlag(String sendFlag) {
		this.sendFlag = sendFlag;
	}
	public String getReceiveTime() {
		return receiveTime;
	}
	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}
}
