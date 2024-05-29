package com.qa.doingerp.parameters;

import java.util.HashMap;

public class CommonData {
	
	public String strProductName;
	public int strQuantity;
	public String strUserName;
	public String strProductCount;

	public String getRunTimeData(String key) {
		return runTimeData.containsKey(key) ? runTimeData.get(key): "No data found for the "+key;
	}

	public void setRunTimeData(String key, String val) {
			this.runTimeData.put(key, val);
	}

	public HashMap<String, String> runTimeData = new HashMap<>();
	
	
	
}
