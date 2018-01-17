package com.cloud.emr.pattern.state;

import com.cloud.emr.pattern.factory.StrategyFactory;
import com.cloud.emr.pattern.strategy.Strategy;
import com.cloud.emr.util.Log;
import com.cloud.emr.util.PropertiesReader;

/**
 * 
 * 项目名称：EMR   
 * 类名称：ReceiveState   
 * 类描述：暂无 
 * 创建人：lw   
 * 创建时间：2014-2-27 下午04:30:33   
 * 修改人：lw   
 * 修改时间：2014-2-28 下午09:40:33   
 * 修改备注： 
 * @version
 */
public class SaveState extends State {

	@Override
	public void handle(Context context) {
		Log.getLogger().i("now begin to save data!");
		Strategy st = new StrategyFactory(context.getRequest().getSession().getServletContext(),context.getData().getAppType()).getInstance();
		boolean bool = st.dealData(context);
		if(bool)
			Log.getLogger().i("save data success,begin to send data!");
		else
			Log.getLogger().e("save data failure,begin to send data!");
		String sendData = PropertiesReader.getProp("sendData");
		if("true".equals(sendData)){
			context.setState(new SendState());
			context.request();
		}else{
			Log.getLogger().i("send data flag is false!");
		}
	}

}
