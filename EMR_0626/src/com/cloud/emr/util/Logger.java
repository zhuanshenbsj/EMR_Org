package com.cloud.emr.util;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Logger {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
	/**
	 * 以当天日期为文件名记录操作日志
	 * @param path 日志文件存放路径
	 * @param log 写入日志的内容
	 */
	public static void writeLog(String path , String log){
		File dir = new File(path+"/log");
		//如果log文件夹不存在，则创建
		if(!dir.exists()){
			dir.mkdirs();
		}
		File f = new File(path+"/log",getToday()+".txt");
		Writer out = null ;	
		try {
			out = new FileWriter(f, true);
			String str = "\r\n"+getCurrentTime()+ " : " + log;
			out.write(str);
			out.close();
			System.out.println(str);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	/**
	 * 将给定格式的时间字符串格式化为：yyyy-MM-dd HH:mm:ss
	 * 例：2013-1-1 1:1:0 经格式化后为：2013-01-01 01:01:00
	 * @param str 格式：yyyy-M-d H:m:s
	 * @return 格式：yyyy-MM-dd HH:mm:ss
	 */
	public static String format(String str){
		try {
			return sdf.format(sdf.parse(str));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获得当前的日期
	 * 格式：yyyy-MM-dd
	 */
	public static String getToday(){
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		String datetime=ft.format(cal.getTime());
		return datetime;
	}
	/**
	 * 获得当前时间
	 * 格式：yyyy-MM-dd HH:mm:ss
	 */
	public static String getCurrentTime() {
		Calendar cal = Calendar.getInstance();
		return sdf.format(cal.getTime());
	}
	/**
	 * 指定时间指定分钟偏移量后的时间
	 * @param str 格式：yyyy-MM-dd HH:mm:ss
	 * @param add 分钟偏移量，正数向后，负数向后
	 * @return 格式：yyyy-MM-dd HH:mm:ss
	 */
	public static String addMinutes(String str,int add){
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(ft.parse(str));
			cal.add(Calendar.MINUTE, add);
			return ft.format(cal.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 根据指定年月日生成指定格式日期字符串
	 * @param year
	 * @param month
	 * @param day
	 * @return yyyy-MM-dd
	 */
	public static String getDate(int year,int month,int day){
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.set(year, month-1, day);
		return ft.format(cal.getTime());
	}
	/**
	 * 根据指定时分秒生成指定格式时间字符串
	 * @param hour
	 * @param minute
	 * @param second
	 * @return HH:mm
	 */
	public static String getTime(int hour,int minute){
		return ""+hour+":"+minute;
	}
	
	public static void main(String[] args) {
//		File file = new File("E:/test.txt");
//		writeLog("e:", "记录日志测试");
		System.out.println(addMinutes("2012-01-02 1:3:0", 117));
		System.out.println(getDate(11,2,29));
		System.out.println(format("2012-01-02 1:3:0"));
	}
}
