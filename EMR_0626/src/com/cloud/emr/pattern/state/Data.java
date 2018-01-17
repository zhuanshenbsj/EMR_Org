package com.cloud.emr.pattern.state;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 
 * 项目名称：EMR   
 * 类名称：Data   
 * 类描述：暂无 
 * 创建人：lw   
 * 创建时间：2014-3-3 下午08:37:27   
 * 修改人：lw   
 * 修改时间：2014-3-3 下午08:37:27   
 * 修改备注： 
 * @version
 */
public class Data {
	private String appType;
	private String dataType;
	private String collectDate;
	private String phone;
	private JSONArray dataValue = new JSONArray();
	@Override
	public String toString() {
		return "--->|phone : "+phone+"|appType : "+appType+"|dataType : "+dataType+"|collectDate : "+collectDate+"|dataValue : "+dataValue.toString();
	}
	/**
	 * @return the appType
	 */
	public String getAppType() {
		return appType;
	}
	/**
	 * @param appType the appType to set
	 */
	public void setAppType(String appType) {
		this.appType = appType;
	}
	/**
	 * @return the dataType
	 */
	public String getDataType() {
		return dataType;
	}
	/**
	 * @param dataType the dataType to set
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	/**
	 * @return the collectDate
	 */
	public String getCollectDate() {
		return collectDate;
	}
	/**
	 * @param collectDate the collectDate to set
	 */
	public void setCollectDate(String collectDate) {
		this.collectDate = collectDate;
	}
	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * @return the dataValue
	 */
	public JSONArray getDataValue() {
		return dataValue;
	}
	/**
	 * @param dataValue the dataValue to set
	 */
	public void setDataValue(JSONArray dataValue) {
		this.dataValue = dataValue;
	}
	
}
