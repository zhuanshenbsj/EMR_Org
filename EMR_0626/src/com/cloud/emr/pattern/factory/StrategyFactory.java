package com.cloud.emr.pattern.factory;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.cloud.emr.pattern.strategy.Strategy;
/**
 * 
 * 项目名称：EMR   
 * 类名称：StrategyFactory   
 * 类描述：暂无 
 * 创建人：lw   
 * 创建时间：2014-3-3 下午08:36:48   
 * 修改人：lw   
 * 修改时间：2014-3-3 下午08:36:48   
 * 修改备注： 
 * @version
 */
public class StrategyFactory {
	
	private Strategy strategy;

	public StrategyFactory(ServletContext sc,String appType) {
		ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(sc);
		strategy = (Strategy)ctx.getBean(appType + "databean"); 
	}

	public Strategy getInstance() {
		return strategy;
	}
}
