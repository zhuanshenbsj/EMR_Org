package com.cloud.emr.util;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;

public class ValueRangeCheck {
	private static double minHighpressure=60;
	private static double maxHighpressure=300;
	private static double minLowpressure=50;
	private static double maxLowpressure=180;
	private static double minHeartrate=40;
	private static double maxHeartrate=160;
	private static double minBeforemeal=0;
	private static double maxBeforemeal=300;
	private static double minAftermeal=0;
	private static double maxAftermeal=300;
	static{
		Properties prop = new Properties();
		try {
			prop.load(new ClassPathResource("com/cmcc/emr/Config/valueRange.properties").getInputStream());
			minHighpressure = Double.parseDouble(prop.getProperty("minHighpressure","0"));
			maxHighpressure = Double.parseDouble(prop.getProperty("maxHighpressure","300"));
			minLowpressure = Double.parseDouble(prop.getProperty("minLowpressure","0"));
			maxLowpressure = Double.parseDouble(prop.getProperty("maxLowpressure","300"));
			minHeartrate = Double.parseDouble(prop.getProperty("minHeartrate","0"));
			maxHeartrate = Double.parseDouble(prop.getProperty("maxHeartrate","300"));
			minBeforemeal = Double.parseDouble(prop.getProperty("minBeforemeal","0"));
			maxBeforemeal = Double.parseDouble(prop.getProperty("maxBeforemeal","300"));
			minAftermeal = Double.parseDouble(prop.getProperty("minAftermeal","0"));
			maxAftermeal = Double.parseDouble(prop.getProperty("maxAftermeal","300"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 检查高压值是否在指定范围
	 * @param value
	 * @return
	 */
	public static boolean checkHighpressure(double value){
		if(value >= minHighpressure && value <= maxHighpressure)
			return true;
		return false;
	}
}
