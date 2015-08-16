package com.demien.weather.ua.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Reports implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7655469207478583975L;
	
	private Map<String, Report> reportMap=new HashMap<String, Report>();

	public Reports() {}

	public Map<String, Report> getReportMap() {
		return reportMap;
	}

	public void setReportMap(Map<String, Report> reportMap) {
		this.reportMap = reportMap;
	}
	
	public void addReport(String cityName, Report report) {
		reportMap.put(cityName, report);
	}
	
	public Report getReport(String cityName) {
		return reportMap.get(cityName);
	}

}
