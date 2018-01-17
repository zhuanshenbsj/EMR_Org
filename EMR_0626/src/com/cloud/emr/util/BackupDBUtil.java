package com.cloud.emr.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;


public class BackupDBUtil {
	
	public static final String EXE = "mysqldump "; // mysqlb备份命令
	String host="";
	String username="";
	String password="";
	String database="";
	String path="";
	HttpServletRequest request;

	public BackupDBUtil(String database, String host, String username,
			String password, String path, HttpServletRequest request) {
		super();
		this.host = host;
		this.username = username;
		this.password = password;
		this.database = database;
		this.path = path;
		this.request = request;
	}
	public BackupDBUtil(String path,HttpServletRequest request){
		this.host = "localhost";
		this.username = PropertiesReader.getDbProp("jdbc.username");
		this.password = PropertiesReader.getDbProp("jdbc.password");
		this.database = "emr";
		this.path = path ;
		this.request = request;
	}

	/**
	 * 备份数据库
	 */
	@SuppressWarnings("finally")
	public int dumpDb() {
		// 备份文件如果存在则先删除
		File file = new File(path);
		if (file.exists()) {
			boolean result_bool = file.delete();
			if(!result_bool)
				Logger.writeLog(request.getSession().getServletContext().getRealPath("/"), "删除备份失败！");
		}
		// 拼接备份命令
		StringBuffer sb = new StringBuffer();
		//操作返回结果
		int result = -1;
		
		sb.append(EXE);
		sb.append(database);
		sb.append(" -h" + host);
		sb.append(" -u" + username );
		sb.append(" -p" + password);
		sb.append(" --set-charset=utf8");
		sb.append(" --result-file=" + path);
		try {
			Process p = Runtime.getRuntime().exec(sb.toString());
			// 获取进程的错误流
			final InputStream is1 = p.getErrorStream();
			// 获取进程的标准输入流
			final InputStream is2 = p.getInputStream();
			// 启动两个线程，一个线程负责读标准输出流，另一个负责读标准错误流
			new Thread() {
				public void run() {
					BufferedReader br1 = new BufferedReader(
							new InputStreamReader(is1));
					try {
						String line1 = null;
						while ((line1 = br1.readLine()) != null) {
							if (line1 != null) {
							}
						}
					} catch (IOException e) {
						try {
							is1.close();
						} catch (IOException e1) {
						}
					} finally {
						try {
							is1.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
			new Thread() {
				public void run() {
					BufferedReader br2 = new BufferedReader(
							new InputStreamReader(is2));
					try {
						String line2 = null;
						StringBuffer stringBuffer = new StringBuffer();
						while ((line2 = br2.readLine()) != null) {
							if (line2 != null) {
								System.out.println(line2);
								stringBuffer.append(line2);
							}
						}
					} catch (IOException e) {
						try {
							is2.close();
						} catch (IOException e1) {
						}
					} finally {
						try {
							is2.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
			result = p.waitFor();
			if (result == 0) {
				Logger.writeLog(request.getSession().getServletContext().getRealPath("/"), "备份成功！");
			}else{
				Logger.writeLog(request.getSession().getServletContext().getRealPath("/"), "result: "+result);
				Logger.writeLog(request.getSession().getServletContext().getRealPath("/"), "备份失败！");
			}
			p.destroy();
		} catch (IOException e) {
			e.printStackTrace();
			Logger.writeLog(request.getSession().getServletContext().getRealPath("/"), e.getMessage());
		} catch (InterruptedException e) {
			Logger.writeLog(request.getSession().getServletContext().getRealPath("/"), e.getMessage());
			e.printStackTrace();
		}finally{
			return result;
		}
	}

}
