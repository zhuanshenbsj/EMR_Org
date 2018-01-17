package com.cloud.emr.util;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;


public class ResponseUtil {
	/**
	 * write to the client  with the value
	 * @param response
	 */
	public static void writeInfo(HttpServletResponse response,String value) {
		try {
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(value);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * write to the client success
	 * @param response
	 */
	public static void writeSuccess(HttpServletResponse response) {
		try {
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write("{\"status\":\"success\"}");
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * write to the client success with the value
	 * @param response
	 */
	public static void writeSuccess(HttpServletResponse response, String value) {
		try {
			System.out.println("{\"status\":\"success\",\"info\":\""+value+"\"}");
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write("{\"status\":\"success\",\"info\":"+value+"}");
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * write to the client failure 
	 * @param response
	 */
	public static void writeFailture(HttpServletResponse response) {
		try {
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write("{\"status\":\"failure\"}");
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * write to the client failure with the value
	 * @param response
	 */
	public static void writeFailture(HttpServletResponse response, String value) {
		try {
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write("{\"status\":\"failure\",\"error\":\""+value+"\"}");
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
