package com.cloud.emr.util;

import org.apache.log4j.Logger;
/**
 * 
 * 项目名称：EMR   
 * 类名称：Log   
 * 类描述：暂无 
 * 创建人：lw   
 * 创建时间：2014-2-27 下午04:44:11   
 * 修改人：lw   
 * 修改时间：2014-2-27 下午04:44:11   
 * 修改备注： 
 * @version
 */

public class Log {
	private static  Logger logger = Logger.getLogger(Log.class);

	private static Log instance = null;

	public static synchronized Log getLogger() {
	if (instance==null)
		instance = new Log();
	return instance;
	}
	public static void info(String message){
		logger.info(message);
	}
	
	public static void error(String message){
		logger.error(message);
	}
	public void i(String message){
		logger.info(message);
	}
	
	public void d(String message){
		logger.debug(message);
	}
	
	public void e(String message){
		logger.error(message);
	}
}
