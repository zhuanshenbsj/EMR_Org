package com.cloud.emr.pattern.strategy;

import org.springframework.stereotype.Component;

import com.cloud.emr.pattern.chain.Handler;
import com.cloud.emr.pattern.chain.NewWsStepCountChainHandler;
import com.cloud.emr.pattern.chain.NewWsStepDetailChainHandler;
import com.cloud.emr.pattern.chain.NewWsStepEffectiveChainHandler;
import com.cloud.emr.pattern.state.Context;
import com.cloud.emr.util.Constants;
import com.cloud.emr.util.Log;
/**
 * 
 * 项目名称：EMR   
 * 类名称：WSStrategy   
 * 类描述：暂无 
 * 创建人：peter   
 * 创建时间：2014-3-3 下午08:37:08   
 * 修改人：peter  
 * 修改时间：2014-7-30 下午02:23:08   
 * 修改备注： milink-sports
 * @version
 */
@Component(Constants.WS_APP_TYPE+"databean")
public class WSStrategy extends Strategy{
	@Override
	public boolean dealData(Context context) {
		Log.getLogger().d("WSStrategy data start save in db !");
		Handler newStepCountChainHandler=new NewWsStepCountChainHandler();
		Handler newStepDetailChainHandler=new NewWsStepDetailChainHandler();
		Handler newstepEffectiveChainHandler=new NewWsStepEffectiveChainHandler();

		newStepCountChainHandler.setSuccessor(newStepDetailChainHandler);
		newStepDetailChainHandler.setSuccessor(newstepEffectiveChainHandler);
		return newStepCountChainHandler.HandleRequest(context);
	}
}
