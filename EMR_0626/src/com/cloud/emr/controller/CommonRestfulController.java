package com.cloud.emr.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cloud.emr.pattern.state.Context;
import com.cloud.emr.service.MongodbService;
import com.cloud.emr.service.ObservationService;
import com.cloud.emr.service.TransportService;
import com.cloud.emr.util.DateUtil;
import com.cloud.emr.util.JsonUtil;
import com.cloud.emr.util.Log;
import com.cloud.emr.util.MongoDBHelper;
import com.cloud.emr.util.PropertiesReader;
import com.cloud.emr.util.ResponseUtil;
import com.cloud.emr.util.SendUtil;
import com.cloud.emr.util.ValidateUtil;
import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
/**
 * 
 * 项目名称：EMR   
 * 
 * 类名称：CommonRestfulController   
 * 
 * 类描述：基于模组的通用数据调取和操作接口
 * 数据处理方式：数据接入，和DADS对接
 * 适用范围：外部接口
 * 
 * 创建人：peter   
 * 

 * 修改备注： 
 * 
 * @version
 */
@Controller
public class CommonRestfulController {
	
	@Autowired
	private ObservationService observationService;
	
	@Autowired
	private TransportService transportService;
	@Autowired
	private MongodbService mongodbService;
	
	private static Logger log = Logger.getLogger(CommonRestfulController.class);
	
	/**
	 * 
	 * 
	 * @param datatype		数据类型:血压、血糖、心电、心功能运动、日常运动已经测试
	 * 
	 * @param measuredate	数据采集日期 (2014-05-28 15:35:00)
	 * 
	 * @param request
	 * 
	 * @param response
	 * 
	 * @return

	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method=RequestMethod.POST, value="/businessDataReceive")
	public void businessDataReceive(HttpServletRequest request,HttpServletResponse response){
		log.info("the start of businessDataReceive ");
		Map result = new HashMap();
		log.info("收到网关DADS发来数据*_*... \r\n");
		String jsonData = "";
		try {
			jsonData = new String((request.getParameter("data").getBytes("iso-8859-1")),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			log.error("receive data occur exception:"+e.getMessage());
		}
		JSONObject jo = JSONObject.fromObject(jsonData);
		String validateInfo = ""+ValidateUtil.checkAppType(JsonUtil.getJsonParamterString(jo, "appType"))+
			(ValidateUtil.isValid(JsonUtil.getJsonParamterString(jo, "dataType"))==true?"":"false")+
				ValidateUtil.checkDateTime(JsonUtil.getJsonParamterString(jo, "collectDate"))+
					(ValidateUtil.isValid(JsonUtil.getJsonParamterString(jo, "phone"))==true?"":"false");
		if("".equals(validateInfo)){
			String isMongo = PropertiesReader.getProp("mongodb");
			System.out.println("isMongo-----------------------------------"+isMongo);
			if("true".equals(isMongo)){
				mongodbService.insertOne(jo);
			}else{
				new Context(request,response,transportService,observationService).request();
			}
		}else{
			response.setStatus(412);
			result.put("status", "数据验证失败！"+validateInfo);
			log.info("the end of businessDataReceive has invalidate param include "+validateInfo);
		}
		response.setStatus(200);
		ResponseUtil.writeInfo(response, JSONObject.fromObject(result).toString());
	}
	

	/**
	 * 
     * 数据处理方式：首先在CONCEPT/PATIENT表中有businessType/uniqueField，appType配置好，可以直接使用
     * 适用范围：外部接口
	 * @param uniqueField 唯一性标识 
	 * 
	 * @param dateTime 记录时间
	 * 
	 * @param businessType 功能类型
	 * 
	 * @param appType 应用类型
	 * 
	 * @param request
	 * 
	 * @param response
	 * 遗留问题：接收心功能远程监护系统的网关数据。
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(method=RequestMethod.POST, value="/businessDataInsert/{uniqueField}/{dateTime}/{businessType}/{appType}",params={"param"})
	public ModelMap businessDataInsert(@PathVariable String uniqueField,@PathVariable String dateTime,@PathVariable String businessType,@PathVariable String appType,
			HttpServletRequest request,HttpServletResponse response){
		Map result = new HashMap();
		log.info("the begin of businessDataInsert... \r\n"+" uniqueField:"+uniqueField+" dateTime:"+dateTime+" businessType:"+businessType+" appType:"+appType);
		String validateInfo = ValidateUtil.checkDateTime(dateTime)+ValidateUtil.checkAppType(appType);
		if("".equals(validateInfo)){
			String params = request.getParameter("param");
			System.out.println(params+"$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		    boolean flag=this.observationService.insertOrUpdateData(uniqueField,DateUtil.formatRestfulDate(dateTime),businessType,appType,params);
		    if(flag){
		    	result.put("status", "success");
		    }else{
		    	result.put("status", "数据插入失败！");
		    }
	    	log.info("the end of businessDataInsert exectue "+(flag?"success":"fail"));
		}else{
			result.put("status", "数据验证失败！"+validateInfo);
			log.info("the end of businessDataInsert has invalidate param include "+validateInfo);
		}
		return new ModelMap(result);
	}
	
	
	/**
     * 适用范围：外部接口
	 * @param request
	 * 
	 * @param response
	 * 
	 */
	@RequestMapping(method = RequestMethod.GET, value="/packDataAutoSend")
	public synchronized void sendPackdata(HttpServletRequest request, HttpServletResponse response){
		Log.getLogger().i("start auto send data...");
		int sum = 0;//计数器
		while (request!=null) {
			List<HashMap<String,String>> list = this.transportService.queryBakPackData();
			int i=0;
			if(list!=null && list.size()==0)
				break;
			for (HashMap<String, String> map : list) {
				this.transportService.sendPackdata(map);
				Log.getLogger().i("ToAPP autoSend data! send "+i+"/"+list.size());
				i++;
			}
			try {
				sum+=list.size();
				Thread.sleep(5000l);
			} catch (InterruptedException e) {
				e.printStackTrace();
				Log.getLogger().e("auto send data thread interrupted, exception message : "+e.getMessage());
			}
			
		}
		try {
			response.getWriter().write("Sent a total of"+sum);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
