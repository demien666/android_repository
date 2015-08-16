package com.demien.weather.ua.domain;

import java.io.Serializable;

public class SearchResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6459251025086663977L;
	private String cityId;
	private String cityName;
	private String cityNameEng;
	private String region;
	private String country;
	
	public SearchResult() {
		
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCityNameEng() {
		return cityNameEng;
	}

	public void setCityNameEng(String cityNameEng) {
		this.cityNameEng = cityNameEng;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public String toString() {
		//return "SearchResult [cityId=" + cityId + ", cityName=" + cityName
		//		+ ", cityNameEng=" + cityNameEng + ", region=" + region
		//		+ ", country=" + country + "]";
		return getCityName()+"("+getRegion()+" "+getCountry()+")";
	}
	
	
	
	
}
