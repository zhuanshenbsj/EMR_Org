package com.cloud.emr.util;

import java.net.ConnectException;
import java.net.SocketException;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
/**
 * 
 * 项目名称：EMR   
 * 类名称：NetworkUtil   
 * 类描述：send data by post way 
 * 创建人：lw   
 * 创建时间：2013-11-21 上午11:59:26   
 * 修改人：lw   
 * 修改时间：2013-11-21 上午11:59:26   
 * 修改备注： 
 * @version
 */
public class NetworkUtil {
	/**
	 * request the uri to post data
	 * 
	 * @param uri 
	 * 				the uri to be posted
	 * @param map
	 * 				parameter map
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean postData(String uri,Map map){
		NameValuePair[] nvs = new NameValuePair[map.size()];
		int i=0;
		for (Iterator ite = map.entrySet().iterator(); ite.hasNext();i++) {
			Map.Entry entry = (Map.Entry) ite.next();
			if(entry.getKey()!=null && entry.getValue()!=null)
				nvs[i] = new NameValuePair(entry.getKey().toString(),entry.getValue().toString());
		}
		return postData(uri,nvs);
	}
	/**
	 * request the uri to post data
	 * 
	 * @param uri
	 * 				the uri to be posted
	 * @param nvs
	 * 				parameter NameValuePair array
	 * @return
	 */
	public static boolean postData(String uri,NameValuePair[] nvs){
		boolean flag = true;
		if(uri==null||nvs==null)
		{
			flag = false;
		}
		PostMethod postM = new PostMethod(uri);
		postM.addParameters(nvs);
		HttpClient client = new HttpClient();
		client.getHttpConnectionManager().getParams().setConnectionTimeout(3000);
		client.getHttpConnectionManager().getParams().setSoTimeout(10000);
		postM.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		try {
			int status = client.executeMethod(postM);
			com.cloud.emr.util.Logger.writeLog(System.getProperty("webapp.emr.root")+"Net Work Log", "request url:"+uri+" response code:"+status);
			if(status != HttpStatus.SC_OK){
				flag = false;
			}
		}catch(ConnectException ce){
			System.out.println("网络请求链接异常! 请求地址："+uri);
			flag = false;
		}catch(SocketException ce){
			System.out.println("网络请求重置异常! 请求地址："+uri);
			flag = false;
		}catch(ConnectTimeoutException cte){
			System.out.println("网络请求链接超时异常! 请求地址："+uri);
			flag = false;
		}catch (Exception e) {
			System.out.println("网络请求异常! 请求地址："+uri);
			e.printStackTrace();
			flag = false;
		}finally{
			postM.releaseConnection();
		}
		return flag;
	}
}
