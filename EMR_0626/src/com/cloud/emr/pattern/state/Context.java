package com.cloud.emr.pattern.state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cloud.emr.service.ObservationService;
import com.cloud.emr.service.TransportService;

/**
 * 
 * 项目名称：EMR   
 * 类名称：Context   
 * 类描述：暂无 
 * 创建人：lw   
 * 创建时间：2014-2-27 下午04:30:24   
 * 修改人：lw   
 * 修改时间：2014-2-27 下午04:30:24   
 * 修改备注： 
 * @version
 */
public class Context {
	private State state;
	private Data data;
	private List<HashMap<String,String>> uri_list = new ArrayList<HashMap<String,String>>();
	private HttpServletRequest request;
	private HttpServletResponse response;
	private TransportService transportService;
	private ObservationService observationService;
	
	public Context(){}
	/**
	 * 
	 * @param request
	 * @param response
	 * @param transportService
	 * @param observationService
	 */
	public Context(HttpServletRequest request,HttpServletResponse response,TransportService transportService,ObservationService observationService){
		this.request = request;
		this.response = response;
		this.transportService = transportService;
		this.observationService = observationService;
		this.state = new ReceiveState();
	}
	public void request(){
		state.handle(this);
	}
	/**
	 * @return the state
	 */
	public State getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(State state) {
		this.state = state;
	}
	/**
	 * @return the data
	 */
	public Data getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(Data data) {
		this.data = data;
	}
	/**
	 * @return the request
	 */
	public HttpServletRequest getRequest() {
		return request;
	}
	/**
	 * @param request the request to set
	 */
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	/**
	 * @return the uri_list
	 */
	public List<HashMap<String, String>> getUri_list() {
		return uri_list;
	}
	/**
	 * @param uri_list the uri_list to set
	 */
	public void setUri_list(List<HashMap<String, String>> uri_list) {
		this.uri_list = uri_list;
	}
	/**
	 * @return the response
	 */
	public HttpServletResponse getResponse() {
		return response;
	}
	/**
	 * @param response the response to set
	 */
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	/**
	 * @return the transportService
	 */
	public TransportService getTransportService() {
		return transportService;
	}
	/**
	 * @return the observationService
	 */
	public ObservationService getObservationService() {
		return observationService;
	}
	
}
