package test;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

import com.cloud.emr.util.DateUtil;



public class RESTfulAdd {
	/**
	 *businessDataInsert/{uniqueField}/{dateTime}/{businessType}/{appType}
	 *WSXHH 血压、血糖、心功、日常
	 *WSSM 睡眠
	 *WSYD 运动等级 （暂不做）
	 *WSJK 血氧、体重
	 *WSXD 心电
	*/
	private static int time=120000;
	private static int num=1;
	private static FileWriter fileWriter;
	public static void main(String[] args) {
		//插入数据
		//insertCommonData(0);
		/*
		 * 插入相同的数据
		 * 每一秒插入一条数据
		 */
		try {
			fileWriter=new FileWriter("e:\\2014_7_11记录日志.txt");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			while(true){
				try {
					Thread.sleep(1000);
					//测试血压
					insertCommonData(1);
					
					//测试血糖
					insertCommonData1(1);
					
					//测试心功能
					insertCommonData2(1);
					
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			
			//insertCommonData3(0);
			try {
				fileWriter.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		
		/*insertCommonData2(0);
		insertCommonData3(0);
		insertCommonData4(0);
		insertCommonData5(0);
		insertCommonData6(0);*/
	}
	/**
	 * 调用RESTful基于模组管理的通用插入接口
	 * @param offset 推前几天
	 */
	
//开始插入血压	
		
		public static void insertCommonData(int offset){//为什么
			
			//接口地址模板
			String url = Constrants.BASEURL+"businessDataInsert/%s/%s/%s/%s?format=json";
			// 地址怎么定义
			//循环插入
			for (int i = 0; i <= offset; i++) {
				//组装时间格式为 yyyyMMddHHmmss  20140611171500
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
				String date=df.format(new Date());
				//String date = DateUtil.getDateBefore(i)+"120000";
				System.out.println("开始插入"+date);
//				组装血压数据
				insertBloodPressCommon(date,url);
//				组装血压数据
				
			}
			System.out.println("插入数据完成 OK"); 
		}
		/**
		 * 插入血压数据
		 * @param date
		 * @param url
		 */
		private static void insertBloodPressCommon(String date,String url){
			//组装URL
			url = String.format(url,Constrants.PHONE,date,"bloodPressure","WSXHH");//在哪里
			System.out.println(url);
			//组装发送数据
			JSONObject jo = new JSONObject();
			jo.put("heartrate","79" );
			jo.put("systolic","120" );
			jo.put("diastolic","80" );
//			调用RESTful的插入方法，发送数据
		
 /*
  * 一次插入30条数据
  */
			/*for(int i=0;i<300;i++){
				
				insertCommon(jo,url);
				
			}*/
			
				insertCommon(jo,url);
			
		}
		
//开始插入血糖		

		public static void insertCommonData1(int offset){//为什么
			
			time++;
			// 地址怎么定义
			String url = Constrants.BASEURL+"businessDataInsert/%s/%s/%s/%s?format=json";
			//循环插入
			for (int i = 0; i <= offset; i++) {
				
				
					//接口地址模板
				
					//组装时间格式为 yyyyMMddHHmmss  20140611171500
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
				String date=df.format(new Date());
				   System.out.println(time);
				   String t=time+"";
					//String date = DateUtil.getDateBefore(i)+t;
					
					System.out.println("开始插入"+date);
					
					
//					组装血糖数据    
					insertBloodGlucoseCommon(date,url);

				
			}
			System.out.println("插入数据完成 OK"); 
		}
		/**
		 * 插入血糖数据
		 * @param date
		 * @param url
		 */
		private static void insertBloodGlucoseCommon(String date,String url){
			//组装URL
		
			url = String.format(url,Constrants.PHONE,date,"bloodGlucose","WSXHH");
			System.out.println(url);
			
			//组装发送数据
			JSONObject jo = new JSONObject();
			jo.put("afterMeal","150" );
			jo.put("beforeMeal","110" );
//			调用RESTful的插入方法，发送数据
		
			
			insertCommon(jo,url);
		}
		
//开始插入心功能运动		

		public static void insertCommonData2(int offset){//为什么
			time++;
			//接口地址模板
			String url = Constrants.BASEURL+"businessDataInsert/%s/%s/%s/%s?format=json";
			// 地址怎么定义
			//循环插入
			int time=120000;
			for (int i = 0; i <= offset; i++) {
				//组装时间格式为 yyyyMMddHHmmss  20140611171500
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置日期格式
				String date=df.format(new Date());
				   String t=time+"";
				  // String date = DateUtil.getDateBefore(i)+t;
				System.out.println("开始插入"+date);
//				组装心功能数据
				insertHeartFunctionCommon(date,url);
//				组装心功能数据
				
			}
			System.out.println("插入数据完成 OK"); 
		}
		/**
		 * 插入心功能数据
		 * @param date
		 * @param url
		 */
		private static void insertHeartFunctionCommon(String date,String url){
			//组装URL
			url = String.format(url,Constrants.PHONE,date,"HeartFunction","WSXHH");
			System.out.println(url);
			//组装发送数据
			JSONObject jo = new JSONObject();
			jo.put("hfStartTime","08:00" );
			jo.put("hfStartHeartRate","79" );
			jo.put("hfEndTime","21:00" );
			jo.put("hfEndHeartRate","120" );
			jo.put("hfSteps","120" );
//			调用RESTful的插入方法，发送数据
			insertCommon(jo,url);
		}
		
		

		
//开始插入日常运动		

				public static void insertCommonData3(int offset){//为什么
					time++;
					//接口地址模板
					String url = Constrants.BASEURL+"businessDataInsert/%s/%s/%s/%s?format=json";// 地址怎么定义
					//循环插入
					for (int i = 0; i <= offset; i++) {
						//组装时间格式为 yyyyMMddHHmmss  20140611171500                 //前几天与删除的日期不能对应
						System.out.println(time);
						   String t=time+"";
							String date = DateUtil.getDateBefore(i)+t;
						System.out.println("开始插入"+date);
//						组装日常运动数据
						insertDailyExerciseCommon11(date,url);
//						组装日常运动数据
						
					}
					System.out.println("插入数据完成 OK"); 
				}
				/**
				 * 插入心功能数据
				 * @param date
				 * @param url
				 */
	private static void insertDailyExerciseCommon11(String date,String url){
					//组装URL
					url = String.format(url,Constrants.PHONE,date,"DailyExercise","WSXHH");
					System.out.println(url);
					//组装发送数据
					JSONObject jo = new JSONObject();
					jo.put("getUpTime","07:30" );
					jo.put("goBedTime","22:30" );
					jo.put("steps","0,2,3```,47" );
					//调用RESTful的插入方法，发送数据
					insertCommon(jo,url);
				}
//开始插入睡眠		
		public static void insertCommonData4(int offset){//为什么
									
			//接口地址模板
	String url = Constrants.BASEURL+"businessDataInsert/%s/%s/%s/%s?format=json";// 地址怎么定义
									//循环插入
									for (int i = 0; i <= offset; i++) {
										//组装时间格式为 yyyyMMddHHmmss  20140611171500                 //前几天与删除的日期不能对应
										String date = DateUtil.getDateBefore(i)+"120000";
										System.out.println("开始插入"+date);
//										组装日常运动数据
										insertSleepCommon(date,url);
//										组装日常运动数据
										
									}
									System.out.println("插入数据完成 OK"); 
								}
								/**
								 * 插入心功能数据
								 * @param date
								 * @param url
								 */
								private static void insertSleepCommon(String date,String url){
									//组装URL
									url = String.format(url,Constrants.PHONE,date,"Sleep","WSSM");
									System.out.println(url);
									//组装发送数据
									JSONObject jo = new JSONObject();
									jo.put("sleepTime","10" );
									jo.put("efficiency","900" );
									jo.put("apneaTimes","30" );
									jo.put("smjd","450,60,2,2,2,2,2,2,2,2,2,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,2,2,2,2,2,2,2,2,2,2,2,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,2,2,2,1,1,1,0,0,0,1,0,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2");
									//调用RESTful的插入方法，发送数据
									insertCommon(jo,url);
								}									
								
//开始插入血氧		

	public static void insertCommonData5(int offset){//为什么
																	
											//接口地址模板
	   String url = Constrants.BASEURL+"businessDataInsert/%s/%s/%s/%s?format=json";// 地址怎么定义
																	//循环插入
																	for (int i = 0; i <= offset; i++) {
																		//组装时间格式为 yyyyMMddHHmmss  20140611171500                 //前几天与删除的日期不能对应
																		String date = DateUtil.getDateBefore(i)+"120000";
																		System.out.println("开始插入"+date);
//																		组装血氧数据
																		insertSPO2Common(date,url);
//																		组装血氧数据
																		
																	}
																	System.out.println("插入数据完成 OK"); 
																}
																/**
																 * 插入血氧数据
																 * @param date
																 * @param url
																 */
																private static void insertSPO2Common(String date,String url){
																	//组装URL
																	url = String.format(url,Constrants.PHONE,date,"SPO2","WSJK");
																	System.out.println(url);
																	//组装发送数据
																	JSONObject jo = new JSONObject();
																	jo.put("pulse","60" );
																	jo.put("oxygen","98" );						//调用RESTful的插入方法，发送数据
																	insertCommon(jo,url);
																}									

//开始插入体重		

public static void insertCommonData6(int offset){//为什么
																							
																	//接口地址模板
	String url = Constrants.BASEURL+"businessDataInsert/%s/%s/%s/%s?format=json";// 地址怎么定义
																							//循环插入
						for (int i = 0; i <= offset; i++) {
																								//组装时间格式为 yyyyMMddHHmmss  20140611171500                 //前几天与删除的日期不能对应
						String date = DateUtil.getDateBefore(i)+"120000";
						System.out.println("开始插入"+date);
//						组装日常运动数据
						insertSleepCommon(date,url);
//						组装日常运动数据
																								
}
	System.out.println("插入数据完成 OK"); 
		}
																						/**
																						 * 插入心功能数据
																						 * @param date
																						 * @param url
																						 */
	private static void insertWeightCommon(String date,String url){
			//组装URL
		url = String.format(url,Constrants.PHONE,date,"Weight","WSSM");
		System.out.println(url);
		//组装发送数据
		JSONObject jo = new JSONObject();
		jo.put("bodyWeight","60" );
	//调用RESTful的插入方法，发送数据
             insertCommon(jo,url);
		}									
	/**
	 * 通用发送数据
	 * @param jo 数据，格式为JSON对象
	 * @param url 发送数据的地址
	 */
	private static void insertCommon(JSONObject jo,String url){
		
		HttpClient client = new HttpClient();
		PostMethod postM = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		try {
			postM = new PostMethod(url);
			postM.setParameter("param", jo.toString());
			System.out.println(jo.toString()+"@@@@@@");
			//执行POST请求
			int executeMethod = client.executeMethod(postM);
			//解析返回结果集
			Reader reader = new InputStreamReader(
					postM.getResponseBodyAsStream(), "utf8");
			BufferedReader br = new BufferedReader(reader);
			StringBuffer buf = new StringBuffer();
			String line = "";
			while (null != (line = br.readLine())) {
				buf.append(line);
			}
			//将结果集复制给变量responseString
			String responseString = buf.toString();
			//打印返回结果
			System.out.println(responseString+"##############");
			
			//记录日志
			
			  fileWriter.write(df.format(new Date())+"第"+num+"次执行插入"+"  ");
			  if(responseString.equals("{\"status\":\"success\"}"))
			  {
			  fileWriter.write("插入成功！"+"\r\n");
			  }
			  else{
				  fileWriter.write("插入失败！"+"\r\n");
			  }
			  
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(null!=postM)
				postM.releaseConnection();//释放连接
		}
		num++;
	}
	
}
