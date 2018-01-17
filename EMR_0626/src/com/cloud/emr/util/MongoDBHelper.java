package com.cloud.emr.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import com.cloud.emr.base.Domain.HealthData;
import com.cloud.emr.controller.CommonRestfulController;
import com.cloud.emr.daoImpl.HealthDataDaoImpl;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientOptions.Builder;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;
/**
 * 
 * 项目名称：EMR   
 * 类名称：MongoDBHelper   
 * 类描述：暂无 
 * 创建人：lw   
 * 创建时间：2014-5-16 上午11:14:05   
 * 修改人：lw   
 * 修改时间：2014-5-16 上午11:14:05   
 * 修改备注： 
 * @version
 */
public class MongoDBHelper {
	/**
	 * hold db object
	 */
	private static DB db;
	
	/**
	 * mongodb name
	 */
	private static String dbname = "emr";
	
	/**
	 * field:
	 * 
	 * phone,appType,dataType,collectDate,dataValue,sendFlag,receiveTime
	 * 
	 */
	private static DBCollection health_data = null;
	
	/**
	 * 
	 * health_data's collection  name
	 * 
	 */
	private static String collection_health_data = "C_HealthData";
	
	/**
	 * field:
	 * 
	 * phone,appType,sex,birth,email,name,deviceId,password,idcard
	 * 
	 */
	private static DBCollection patient_data = null;
	
	/**
	 * patient_data's collection  name
	 */
	private static String collection_patient_data = "C_PatientInfo";
	
	/*
	 * single instance
	 */
	private static MongoDBHelper uniqueInstance = null;
	
	/**
	 * mongodb's db config file path
	 */
	private static String propertiesPath = "com/cmcc/emr/Config/mongodb.properties";
	
	/**
	 * default constructer
	 */
	private static Logger log = Logger.getLogger(CommonRestfulController.class);
	private MongoDBHelper(){
		
	}
	
	/**
	 * get single instance
	 * @return
	 */
	public static MongoDBHelper getInstance() {
		long start = System.currentTimeMillis();
		if (uniqueInstance == null) {
			Properties prop = new Properties();
			InputStream fis = null;
			try {
				fis =  MongoDBHelper.class.getClassLoader().getResourceAsStream(propertiesPath);
				prop.load(fis);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			uniqueInstance = new MongoDBHelper();
			MongoClient mongoClient;
			try {
				Builder bd = MongoClientOptions.builder();
				bd.connectionsPerHost(Integer.parseInt(prop.getProperty("mongo.connectionsPerHost")));
				bd.threadsAllowedToBlockForConnectionMultiplier(Integer.parseInt(prop.getProperty("mongo.threadsAllowedToBlockForConnectionMultiplier")));
				bd.connectTimeout(Integer.parseInt(prop.getProperty("mongo.connectTimeout")));
				bd.maxWaitTime(Integer.parseInt(prop.getProperty("mongo.maxWaitTime")));
				bd.socketKeepAlive(Boolean.parseBoolean(prop.getProperty("mongo.socketKeepAlive")));
				bd.socketTimeout(Integer.parseInt(prop.getProperty("mongo.socketTimeout")));
				MongoClientOptions mo = bd.build();
				mongoClient = new MongoClient(new ServerAddress(prop.getProperty("mongo.host"), Integer.parseInt(prop.getProperty("mongo.port"))),mo);
				mongoClient.setWriteConcern(WriteConcern.SAFE);
				db = mongoClient.getDB(dbname );
				init();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			System.out.println("getInstance cost :"+(System.currentTimeMillis()-start)+"ms");
		}
		return uniqueInstance;
	}
	
	/**
	 * initialize MongoDBHelper Instance
	 */
	private static void  init(){
		//config health data
		createHealthDataIndex();
		//config patient data
		createPatientDataIndex();
	}
	
	/**
	 * create healthdata collection index
	 */
	private static void createHealthDataIndex(){
		String[] index_and_order = {"phone,1","appType,1","dataType,1","measureTime,1"};
		health_data = db.getCollection(collection_health_data);
		createIndex(health_data, index_and_order);
	}
	
	/**
	 * create healthdata collection index
	 */
	private static void createPatientDataIndex(){
		String[] index_and_order = {"phone,1","appType,1"};
		patient_data = db.getCollection(collection_patient_data);
		createIndex(patient_data, index_and_order);
	}
	
	/**
	 * common create index method
	 * 
	 * @param collectionName
	 * 				the index to be created name
	 * 
	 * @param indexs 
	 * 				like {"phone,1","appType,1"} and 1 means asc, 0 means desc
	 * 
	 */
	private static void createIndex(DBCollection collection,String[] indexs){
		long start = System.currentTimeMillis();
		for (int i = 0; i < indexs.length; i++) {
			List<DBObject> index_list = collection.getIndexInfo();
			for (DBObject dbObject : index_list) {
				BasicDBObject bd = (BasicDBObject)dbObject;
				JSONObject jo = JSONObject.fromObject(bd.toString());
				JSONObject key = jo.getJSONObject("key");
				if(!key.containsKey(indexs[i])){
					 String[] ix = indexs[i].split(",");
					 collection.createIndex(new BasicDBObject(ix[0], Integer.parseInt(ix[1])));
				}
			}
		}
		System.out.println("create index cost :"+(System.currentTimeMillis()-start)+"ms");
	}
	
	/**
	 * save health data 
	 * 			
	 * @param bd
	 * 				you can save one or more BasicDBObject Object
	 * @return		
	 * 				the last time save id
	 * 				
	 */
	public String insertHealthData(BasicDBObject... bd){
		System.out.print("----------------insertHealthData BasicDBObject begin---------------");
		String id = "";
		for (int i = 0; i < bd.length; i++) {
			DBObject dj = new BasicDBObject();
			dj.put("phone", bd[i].getString("phone"));
			dj.put("appType", bd[i].getString("appType"));
			dj.put("dataType", bd[i].getString("dataType"));
			Object dataValue = bd[i].get("dataValue");

			WriteResult wr=null;
			//保证update操作和getLastError()使用同一个连接，并且减少了一次存/取连接的过程
			db.requestStart(); //开始数据库连接
			try {
				db.requestEnsureConnection();//保持数据库连接
				DBObject upsertValue=new BasicDBObject("$set", bd[i]);
				wr = health_data.update(dj, upsertValue, true, false);
				System.out.print("----------------update success---------------");
			} catch (Exception e) {
				/*Log.getLogger().e("insert error");
				Log.getLogger().e(e.getMessage());*/
				log.error("插入数据发生异常");
				log.error(e.getMessage());
				DBObject err = db.getLastError();
				if(err!=null)
				return "0";
			}
			finally{
				db.requestDone();//结束数据库连接
			}
			if(i==bd.length-1)//last insert data
			{
				if(wr.isUpdateOfExisting()){//update data
					id =  this.queryHealthDataId(dj);
					Log.getLogger().i("execute update health ,id is :"+id);
				}else{
					id =  wr.getUpsertedId().toString();
				}
			}

		}
		return id;
		
	}
	/**
	 * over load insertHealthData method
	 * 
	 * @param js
	 * 				support one or more JSONObject save in mongodb
	 * @return 
	 * 				the last time save id
	 */
	@SuppressWarnings("unchecked")
	public String insertHealthData(JSONObject... js){
		System.out.print("----------------insertHealthData begin---------------");
		BasicDBObject[] bds = new BasicDBObject[js.length];
		for (int i =0,n=js.length;i<n;i++) {
			BasicDBObject obj = new BasicDBObject();
			Iterator<String> joKeys = js[i].keys();  
			while(joKeys.hasNext()){  
				String key = joKeys.next();
				obj.put(key, js[i].get(key));  
			}
			bds[i]=obj;
		}
		return insertHealthData(bds);
		
	}
	
	
	
	
	
	/**
	 * query health data id by condition
	 * 
	 * @param db_condition
	 * 
	 * @return
	 * 
	 */
	public String queryHealthDataId(DBObject db_condition){
		DBObject db_filed = new BasicDBObject();
		db_filed.put("_id", true);
		BasicDBObject oid = (BasicDBObject)health_data.findOne(db_condition, db_filed);
		return oid.getObjectId("_id").toString();
	}
	
	/**
	 * query health data by condition
	 * 
	 * @param uniqueField
	 * 
	 * @param startTime
	 * 
	 * @param endTime
	 * 
	 * @param startNum
	 * 
	 * @param offset
	 * 
	 * @param dataType
	 * 
	 * @param appType
	 * 
	 * @return
	 * 
	 */
	public List<DBObject> queryHealthDataByCondition(String uniqueField,String startTime,String endTime,int startNum,int offset,String dataType,String appType){
		DBObject db_condition = new BasicDBObject();
		db_condition.put("phone", uniqueField);
		db_condition.put("appType", appType);
		db_condition.put("dataType", dataType);
		BasicDBObject gtlt = new BasicDBObject();
		gtlt.put("$gt", startTime+" 23:59:59");
		gtlt.put("$lt", endTime+" 00:00:00");
		db_condition.put("dataValue.measureTime",gtlt);
		DBObject db_filed = new BasicDBObject();
		if(appType.equals(Constants.ECG_APP_TYPE)){
			db_filed.put("dataValue.measureTime", true);
			db_filed.put("dataValue.hr", true);
			db_filed.put("dataValue.waveForm", true);
			db_filed.put("dataValue.heartRate", true);
			db_filed.put("dataValue.isArrhythmia", true);
			db_filed.put("dataValue.stIsNormal", true);
			db_filed.put("dataValue.isAF", true);
			db_filed.put("dataValue.detailedResults", true);
			db_filed.put("dataValue.simpleResult", true);
		}else{
			db_filed.put("dataValue", true);
		}
		return health_data.find(db_condition, db_filed).skip(startNum).limit(offset).toArray();
	}
	
	/**
	 * query ecg data for chart show 
	 * 
	 * @param phone
	 * 
	 * @param date
	 * 
	 * @param appType
	 * 
	 * @return
	 * 
	 */
	public List<DBObject> queryEcgDataByCondition(String phone,String date,String appType){
		DBObject db_condition = new BasicDBObject();
		db_condition.put("phone", phone);
		db_condition.put("appType", appType);
		BasicDBObject gtlt = new BasicDBObject();
		gtlt.put("$gt", DateUtil.getBeforeDay(date)+" 23:59:59");
		gtlt.put("$lt", DateUtil.getNextDay(date)+" 00:00:00");
		db_condition.put("dataValue.measureTime",gtlt);
		DBObject db_filed = new BasicDBObject();
		db_filed.put("dataValue.measureTime", true);
		db_filed.put("dataValue.rawData", true);
		return health_data.find(db_condition, db_filed).toArray();
	}
	
	/**
	 * query sleep data for chart show 
	 * 
	 * @param phone
	 * 
	 * @param startTime like '2014-05-29'
	 * 
	 * @param endTime like '2014-05-29'	
	 * 
	 * @return
	 * 
	 */
	public List<DBObject> querySleepDataDayByCondition(String phone,String startTime,String endTime){
		DBObject db_condition = new BasicDBObject();
		db_condition.put("phone", phone);
		db_condition.put("appType", Constants.SLEEP_APP_TYPE);
		db_condition.put("dataType", Constants.SLEEP_APP_BUSINESS_TYPE);
		BasicDBObject gtlt = new BasicDBObject();
		gtlt.put("$gt", DateUtil.getBeforeDay(startTime)+" 23:59:59");
		gtlt.put("$lt", DateUtil.getNextDay(endTime)+" 00:00:00");
		db_condition.put("dataValue.measureTime",gtlt);
		DBObject db_filed = new BasicDBObject();
		db_filed.put("dataValue.deep", true);
		db_filed.put("dataValue.rem", true);
		db_filed.put("dataValue.wakeTimes", true);
		db_filed.put("dataValue.measureTime", true);
		return health_data.find(db_condition, db_filed).toArray();
	}
	/**
	 * query sleep data for chart show 
	 * 
	 * @param phone
	 * 
	 * @param queryDate like '2014-05-29'
	 * 
	 * @return
	 * 
	 */
	public DBObject querySleepDataByCondition(String phone,String queryDate){
		DBObject db_condition = new BasicDBObject();
		db_condition.put("phone", phone);
		db_condition.put("appType", Constants.SLEEP_APP_TYPE);
		db_condition.put("dataType", Constants.SLEEP_APP_BUSINESS_TYPE);
		BasicDBObject gtlt = new BasicDBObject();
		gtlt.put("$gt", DateUtil.getBeforeDay(queryDate)+" 23:59:59");
		gtlt.put("$lt", DateUtil.getNextDay(queryDate)+" 00:00:00");
		db_condition.put("dataValue.measureTime",gtlt);
		DBObject db_filed = new BasicDBObject();
		db_filed.put("dataValue", true);
		return health_data.findOne(db_condition, db_filed);
	}
	
	/**
	 * 
	 * query health Manage Data 
	 * 
	 * @param dataType
	 * 					like 'bloodPressure'
	 * @param startTime
	 * 					query start date like '2014-05-25'
	 * @param endTime
	 *					query end date like '2014-05-26'
	 * @param startNum
	 * 					query start number
	 * @param offset
	 * 					query number
	 * 
	 * @return  List <"DBObject"> 
	 * 
	 */
	public List<DBObject> queryHealthManageData(String dataType,String startTime,String endTime,int startNum,int offset){
		DBObject db_condition = new BasicDBObject();
		db_condition.put("dataType", dataType);
		BasicDBObject gtlt = new BasicDBObject();
		gtlt.put("$gt", startTime+" 23:59:59");
		gtlt.put("$lt", endTime+" 00:00:00");
		db_condition.put("dataValue.measureTime",gtlt);
		DBObject db_filed = new BasicDBObject();
		if(dataType.equals(Constants.ECG_APP_BUSINESS_TYPE)){
			db_filed.put("dataValue.measureTime", true);
			db_filed.put("dataValue.hr", true);
			db_filed.put("dataValue.waveForm", true);
			db_filed.put("dataValue.heartRate", true);
			db_filed.put("dataValue.isArrhythmia", true);
			db_filed.put("dataValue.stIsNormal", true);
			db_filed.put("dataValue.isAF", true);
			db_filed.put("dataValue.detailedResults", true);
			db_filed.put("dataValue.simpleResult", true);
			db_filed.put("phone", true);
			db_filed.put("appType", true);
		}else{
			db_filed.put("dataValue", true);
			db_filed.put("phone", true);
			db_filed.put("appType", true);
		}
		return health_data.find(db_condition, db_filed).skip(startNum).limit(offset).toArray();
	}
	
	/**
	 * 
	 * query count by condition
	 * 
	 * @param appType like 'WSSM'
	 * 
	 * @param dataType like 'sleep'
	 * 
	 * @param day  like '2014-05-28'
	 * 
	 * @return
	 */
	public long queryHealthDataCountByAppTypeAndDataTypeAndDate(String appType,String dataType,String day){
		DBObject db_condition = new BasicDBObject();
		db_condition.put("appType", appType);
		db_condition.put("dataType", dataType);
		BasicDBObject gtlt = new BasicDBObject();
/*		gtlt.put("$gt", DateUtil.getBeforeDay(day));
		gtlt.put("$lt", DateUtil.getNextDay(day));
//		db_condition.put("dataValue.measureTime",gtlt);
 *   

*/		System.out.println("queryHealthDataCountByAppTypeAndDataTypeAndDate begin");
		return health_data.count(db_condition);
	}
	
	/**
	 * query user send data count
	 * 
	 * @param appType like 'WSXD'
	 * 
	 * @param dataType like 'ecg'
	 * 
	 * @param day  like '2014-05-28'
	 * 
	 * @return
	 * 
	 */
	@SuppressWarnings("rawtypes")
	public long queryHealthDataUserCountByAppTypeAndDataTypeAndDate(String appType,String dataType,String day){
		DBObject db_condition = new BasicDBObject();
		db_condition.put("appType", appType);
		db_condition.put("dataType", dataType);
		BasicDBObject gtlt = new BasicDBObject();
		gtlt.put("$gt", DateUtil.getBeforeDay(day)+" 23:59:59");
		gtlt.put("$lt", DateUtil.getNextDay(day)+" 00:00:00");
		db_condition.put("dataValue.measureTime",gtlt);
		List list = health_data.distinct("phone", db_condition);
		if(list!=null){
			long length = list.size();
			list = null;
			return length;
		}else{
			return 0l;
		}
	}
	
	/**
	 * query needed send data by condition
	 * 
	 * @param appType
	 * 	
	 * @param sendFlag
	 * 
	 * @return List<DBObject> default 1000
	 * 
	 */
	public List<DBObject> queryHealthDataByApptypeAndSendFlag(String appType,String sendFlag){
		return queryHealthDataByApptypeAndSendFlag(appType,sendFlag,1000);
	}
	
	/**
	 * query needed send data by condition
	 * 
	 * @param appType
	 * 	
	 * @param sendFlag
	 * 
	 * @return List<DBObject> default 1000
	 * 
	 */
	public List<DBObject> queryHealthDataByApptypeAndSendFlag(String appType,String sendFlag,int limit){
		DBObject db_condition = new BasicDBObject();
		db_condition.put("appType", appType);
		db_condition.put("sendFlag", sendFlag);
		DBObject db_filed = new BasicDBObject();
		db_filed.put("deviceId", false);
		db_filed.put("sendFlag", false);
		db_filed.put("receiveTime", false);
		return health_data.find(db_condition, db_filed).limit(limit).toArray();
	}
	
	/**
	 * update sendFlag 
	 * 
	 * @param ObjectId 
	 * 					object id like 53717e35efeac759455005a3
	 * 
	 * @param sendFlag
	 * 					update value
	 * 
	 * @return DBObject
	 * 
	 */
	public DBObject updateHealthDataSendFlagByObjectId(String ObjectId ,String sendFlag){
		DBObject query = new BasicDBObject();
		query.put("_id", new ObjectId(ObjectId));
		DBObject update = new BasicDBObject();
		update.put("sendFlag", sendFlag);
    	DBObject updateSetValue=new BasicDBObject("$set",update);
		return health_data.findAndModify(query, updateSetValue);
	}
	
	/**
	 * save health data 
	 * 			
	 * @param bd
	 * 				you can save one or more BasicDBObject Object
	 * @return		
	 * 				effect lines in mongodb
	 * 				
	 */
	public String insertPatientInfo(BasicDBObject... bd){
		
		WriteResult wr=null;
		String id="0";
		DBObject upsertValue=new BasicDBObject("$set", bd[0]);
		wr = patient_data.update(bd[0], upsertValue, true, false);
			if(wr.isUpdateOfExisting()){//update data
				id =  this.queryPatientInfoId(bd[0]);
				Log.getLogger().i("execute update patient ,id is :"+id);
			}else{
				id =  wr.getUpsertedId().toString();
			}
		return id;
	}
	@SuppressWarnings("unchecked")
	public String insertPatientInfo(JSONObject... js){
		BasicDBObject[] patients = new BasicDBObject[js.length];
		for (int i =0,n=js.length;i<n;i++) {
			BasicDBObject obj = new BasicDBObject();
			Iterator<String> joKeys = js[i].keys();  
			while(joKeys.hasNext()){  
				String key = joKeys.next();
				obj.put(key, js[i].get(key));  
			}
			patients[i]=obj;
		}
		return insertPatientInfo(patients);
	}
	/*
	 * 获取用户ID
	 * 
	 */
	public String queryPatientInfoId(DBObject db_condition){
		DBObject db_filed = new BasicDBObject();
		db_filed.put("_id", true);
		BasicDBObject oid = (BasicDBObject)patient_data.findOne(db_condition, db_filed);
		return oid.getObjectId("_id").toString();
	}
	/**
	 * over load insertPatientData method
	 * 
	 * @param js
	 * 				support one or more JSONObject save in mongodb
	 * @return
	 */
	public String insertPatientData(JSONObject... js){
		return insertDBCollectionData(patient_data,js);
	}
	
	/**
	 * query patient's appType and phone
	 * 
	 * @param js
	 * 				support one or more JSONObject save in mongodb
	 * @return
	 */
	public DBObject queryPatientPhoneAndApptypeByDeviceId(String deviceId){
		DBObject db_condition = new BasicDBObject();
		db_condition.put("deviceId", deviceId);
		DBObject db_filed = new BasicDBObject();
		db_filed.put("appType", true);
		db_filed.put("phone", true);
		return patient_data.findOne(db_condition, db_filed);
	}
	
	/**
	 * common insert collection method
	 * 
	 * @param dbCollection
	 * 				
	 * 				DBCollection instance
	 * @param js
	 * 				support one or more JSONObject save in mongodb
	 * 
	 * @return
	 * 				the last time save id
	 */
	@SuppressWarnings("unchecked")
	public String insertDBCollectionData(DBCollection dbCollection,JSONObject... js){
		BasicDBObject[] bds = new BasicDBObject[js.length];
		for (int i =0,n=js.length;i<n;i++) {
			BasicDBObject obj = new BasicDBObject();
			Iterator<String> joKeys = js[i].keys();  
			while(joKeys.hasNext()){  
				String key = joKeys.next();
				obj.put(key, js[i].get(key));  
			}
			bds[i]=obj;
		}
		dbCollection.insert(bds);
		return bds[0].getString("_id");
	}
	
	/**
	 * save data in file with UTF-8 EnCodeing
	 * 
	 * @param fileName
	 * 					fileName and full path be required
	 * @param data
	 *  				JSON data 
	 * @return
	 */
	public static boolean backUpDataInfile(String fileName,String data){
		boolean re_bool = true;
		File file = new File(fileName);
		try {
			FileUtils.writeStringToFile(file, data, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
			re_bool = false;
		}
		return re_bool;
	}
	
	/**
	 * read data from file 
	 * 
	 * @param fileName
	 * 					fileName and full path be required
	 * @param data
	 *  				JSON data with UTF-8 EnCodeing
	 * @return
	 */
	public static String readDataFromfile(String fileName){
		String re_str = "";
		File file = new File(fileName);
		try {
			FileUtils.readFileToString(file, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
			re_str="error";
		}
		return re_str;
	}
	public static void main(String[] args) {
		MongoDBHelper.getInstance();
		MongoDBHelper mdb = new MongoDBHelper();
		System.out.println("____________________________");
		JSONObject jo = new JSONObject();
		
		jo.put("phone", "13454151");
		jo.put("appType", "wsyd");
		jo.put("dataType","stepCount");
		jo.put("measureTime","2017-04-21");
		Object[] array = new Object[] { "JSON", 1, 2, 3 };   
		JSONArray jsonArray = JSONArray.fromObject(array);   
		jo.put("dataValue",jsonArray);   

		mdb.insertHealthData(jo);
		long sum=mdb.queryHealthDataCountByAppTypeAndDataTypeAndDate("wsyd", "stepCount", "");
		System.out.println(sum);
	}
}
