package com.cloud.emr.pattern.state;


/**
 * 
 * 项目名称：EMR   
 * 类名称：State   
 * 类描述：暂无 
 * 创建人：lw   
 * 创建时间：2014-2-27 下午04:30:29   
 * 修改人：lw   
 * 修改时间：2014-2-27 下午04:30:29   
 * 修改备注： 
 * @version
 */
public abstract class State {
	public abstract void handle(Context context);
}
