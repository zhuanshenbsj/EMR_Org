package com.cloud.emr.pattern.strategy;

import com.cloud.emr.pattern.state.Context;
import com.cloud.emr.pattern.state.Data;
/**
 * 
 * 项目名称：EMR   
 * 类名称：Strategy   
 * 类描述：暂无 
 * 创建人：lw   
 * 创建时间：2014-3-3 下午08:37:03   
 * 修改人：lw   
 * 修改时间：2014-3-3 下午08:37:03   
 * 修改备注： 
 * @version
 */
public abstract class Strategy {
	public abstract boolean dealData(Context context);
}
