package com.cloud.emr.util;



public class Constants {

	// pager default parameters
	public final static int pageSize = 10;
	public final static int pageNo =1;
	
	// default query record num
	public final static int startNum =1;
	public final static int offset = 30;
	
	public final static String LOGINED = "userInfo";
	
	//define data transport
	public final static String TX_APP_TYPE = "WSXHH";
	public final static String WS_APP_TYPE = "WSYD";
	public final static String CD_APP_TYPE = "WSJK";
	public final static String SLEEP_APP_TYPE = "WSSM";
	public final static String ECG_APP_TYPE = "WSXD";
	
	//define businessType
	public final static String ECG_APP_BUSINESS_TYPE = "ECG";
	public final static String SLEEP_APP_BUSINESS_TYPE = "sleep";
	public final static String CD_SPO2_APP_BUSINESS_TYPE = "SPO2";
	public final static String CD_WEIGHT_APP_BUSINESS_TYPE = "weight";
	public final static String TX_BLOODSUGAR_APP_BUSINESS_TYPE = "bloodGlucose";
	public final static String TX_BLOODPRESSURE_APP_BUSINESS_TYPE = "bloodPressure";
	public final static String TX_HEARTFUNCTION_APP_BUSINESS_TYPE = "heartFunction";
	public final static String TX_DAILYEXERCISE_APP_BUSINESS_TYPE = "dailyExercise";
	public final static String WS_RUNMESSAGE_APP_BUSINESS_TYPE = "runmessage";
	
	//新增我尚运动字段
	public final static String WS_steptCount_APP_BUSINESS_TYPE = "stepCount";
	public final static String WS_stepDetail_APP_BUSINESS_TYPE = "stepDetail";
	public final static String WS_stepEffective_APP_BUSINESS_TYPE = "stepEffective";
	
	
	/**
	 * 血压
	 */
	public final static String DATATYPE_BLOODPRESSURE = PropertiesReader.getProp("DATATYPE_BLOODPRESSURE");
	
	/**
	 * 详细包
	 */
	public final static String DATATYPE_STEPDETAIL = PropertiesReader.getProp("DATATYPE_STEPDETAIL");
	
	/**
	 * 有效步数
	 */
	public final static String DATATYPE_STEPEFFECTIVE = PropertiesReader.getProp("DATATYPE_STEPEFFECTIVE");

	/**
	 * 简要包
	 */
	public final static String DATATYPE_STEPCOUNT = PropertiesReader.getProp("DATATYPE_STEPCOUNT");
}
