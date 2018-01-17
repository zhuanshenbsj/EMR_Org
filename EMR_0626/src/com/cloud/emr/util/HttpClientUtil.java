package com.cloud.emr.util;

import java.util.Arrays;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;


public class HttpClientUtil {
	public static boolean sendHttpData(String className, String url, NameValuePair[] parameter){
		HttpClient client = new HttpClient(); 
		PostMethod post = new PostMethod(url);
		boolean isSuccess=true; 
		try {
			post.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			post.setRequestBody(parameter); 
			Log.info(className+" send data : "+Arrays.deepToString(parameter));
			//设置连接超时时间
			client.getHttpConnectionManager().getParams().setConnectionTimeout(3000);
			//设置响应超时时间
			client.getHttpConnectionManager().getParams().setSoTimeout(15000);
			int returnFlag = client.executeMethod(post);
			if(returnFlag!=200){
				isSuccess=false;
			}
			Log.info(className+" success receive form post: " + post.getStatusLine().toString()+ ",returnFlag="+returnFlag);      
		} catch (Exception e) {
			e.printStackTrace();
			isSuccess=false;
			Log.info(className+" fail receive form post: " +e.getMessage());
		}finally{
			if(post!=null){
				post.releaseConnection();
				Log.info(className+" post.releaseConnection() " + "is coming");    				
			}
		}
		return isSuccess;
	}
}
