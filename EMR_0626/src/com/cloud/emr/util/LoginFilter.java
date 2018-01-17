package com.cloud.emr.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ralasafe.Ralasafe;
import org.ralasafe.WebRalasafe;
import org.ralasafe.privilege.Privilege;
import org.ralasafe.servlet.RalasafeController;
import org.ralasafe.servlet.WebUtil;
import org.ralasafe.user.User;

public class LoginFilter implements Filter {

	HttpServletRequest req = null;
	HttpServletResponse res = null;

	public LoginFilter() {
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		req = (HttpServletRequest) request;
		res = (HttpServletResponse) response;
		String path = req.getContextPath();
		String basePath = req.getScheme() + "://"
				+ req.getServerName() + ":" + req.getServerPort()
				+ path + "/";
		// 获得请求的全路径
		String reUrl = req.getRequestURL().toString();
		// 是否包含参数
		int index = reUrl.indexOf("?");
		String re = "";
		if (index == -1) {
			re = reUrl.substring(basePath.length());
		} else {
			re = reUrl.substring(basePath.length(), index);
		}
		// 是否登录
		if (req.getSession().getAttribute("userInfo") == null) {
			req.getRequestDispatcher("/main.html").forward(req,res);
			return;
		} else {//是否有权限
			org.ralasafe.user.User user = WebRalasafe.getCurrentUser(req);
			boolean hasPrivilegeAdmin = Ralasafe.hasPrivilege(RalaConstants.QUERY_ADMIN_HEAD, user);
			if (!hasPrivilegeAdmin) {
				req.getRequestDispatcher("/main.html").forward(request,
						res);
				// 必须加返回，否则报错
				return;
			}
		}
		// 继续执行
		chain.doFilter(req, res);
	}

	public void init(FilterConfig fConfig) throws ServletException {
	}

}
