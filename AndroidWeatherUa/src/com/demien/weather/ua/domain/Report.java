package com.demien.weather.ua.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Report implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3970372461083719496L;
	private String cityName;
	private String regionName;
	private String countryName;

	private List<Day> dayReports;

	public Report() {
		dayReports = new ArrayList<Day>();
	}

	public void addDayReport(Day day) {
		dayReports.add(day);
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public List<Day> getDayReports() {
		return dayReports;
	}

	public void setDayReports(List<Day> dayReports) {
		this.dayReports = dayReports;
	}
	
	public Report clone() {
		Report result=new Report();
		result.setCityName(this.getCityName());
		result.setRegionName(this.getRegionName());
		result.setCountryName(this.getCountryName());
		return result;
	}

	@Override
	public String toString() {
		String s = "";
		for (Day d : dayReports) {
			s = s + d.toString() + "\n";
		}
		return "Report [cityName=" + cityName + ", regionName=" + regionName
				+ ", countryName=" + countryName + ", dayReports=\n" + s + "]";
	}

}
