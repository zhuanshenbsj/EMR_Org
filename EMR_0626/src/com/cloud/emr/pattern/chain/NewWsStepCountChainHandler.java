package com.cloud.emr.pattern.chain;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import com.cloud.emr.pattern.state.Context;
import com.cloud.emr.pattern.state.Data;
import com.cloud.emr.util.Constants;
import com.cloud.emr.util.JsonUtil;
import com.cloud.emr.util.Log;

/**
 * 
 * 项目名称：EMR   
 * 类名称：NewWsStepDetailChainHandler  
 * 类描述：暂无 
 * 创建人：lsf   
 * 创建时间：2014-7-30    
 * 修改人：lsf 
 * 修改时间：
 * 修改备注： 
 * @version
 */

public class NewWsStepCountChainHandler extends Handler {

	@Override
	public boolean HandleRequest(Context context) {
		String dataType = context.getData().getDataType();
		//判断是否是简要步数
		if(Constants.WS_steptCount_APP_BUSINESS_TYPE.equalsIgnoreCase(dataType)){
			Data data = context.getData();
			Log.getLogger().d("data deal by NewWsStepCountChainHandler !");
			if(data.getDataValue()!=null && data.getDataValue().size()!=0){
				Map<String,Object> map = new HashMap<String,Object>();
				System.out.println(data.getDataValue().getJSONObject(0));
				String measureTime = JsonUtil.getJsonParamterString(data.getDataValue().getJSONObject(11), "measureTime");
				map.put("stepCount", data.getDataValue());
				//insert into database
				boolean bool = context.getObservationService().insertOrUpdateData(data.getPhone(), measureTime, 
						Constants.WS_steptCount_APP_BUSINESS_TYPE, Constants.WS_APP_TYPE, JSONObject.fromObject(map).toString(),data.getCollectDate());
				if(!bool)
					Log.getLogger().e("NewWsStepCountChainHandler save data into db error!");
				return bool;
			}
			else{
				Log.getLogger().e("NewWsStepCountChainHandler datavalue is null!");
				return false;
			}
		}
		else{
			if(successor!=null)
				return successor.HandleRequest(context);
			else
				return false;
		}
	}

}
