package com.cloud.emr.util;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
/**
 * 
 * 项目名称：EMR   
 * 类名称：EmrInitServlet   
 * 类描述：暂无 
 * 创建人：lw   
 * 创建时间：2014-1-13 下午02:25:18   
 * 修改人：lw   
 * 修改时间：2014-1-13 下午02:25:18   
 * 修改备注： 
 * @version
 */
public class EmrInitServlet extends HttpServlet {
private static final long serialVersionUID = 5406753802213891396L;

@Override
	public void init() throws ServletException {
		super.init();
		try {
			String path = PropertiesReader.getProp("xml_path");
			CreateFileUtil.del(path);
			String classPath = this.getClass().getClassLoader().getResource("").getPath();
			CreateFileUtil.copyDir(classPath.replace("%20", " ")+"com/cmcc/emr/Config/xml", path);
			System.out.println("initialize ralasafe config success！");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("initialize ralasafe config occur exception！");
			System.out.println(e.getMessage());
		}
	}
}
