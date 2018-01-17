package com.cloud.emr.util;

import java.util.Map;

import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

public class MyMappingJacksonJsonView extends MappingJacksonJsonView{
	/**
	 * modify the old method to support single result
	 */
	@Override
	protected Object filterModel(Map<String, Object> model) {    
	    Map<?, ?> result = (Map<?, ?>) super.filterModel(model);    
	    if (result.size() == 1) {    
	        return result.values().iterator().next();    
	    } else {    
	        return result;    
	    }    
	}    
}
