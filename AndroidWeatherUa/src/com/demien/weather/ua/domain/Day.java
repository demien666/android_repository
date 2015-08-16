package com.demien.weather.ua.domain;

import java.io.Serializable;

public class Day implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 345259535248162804L;
	private String date;
	private String hour;
	private String cloud;
	private String pict;
	private String ppcp;
	private String tMin;
	private String tMax;
	private String pMin;
	private String pMax;
	private String windMin;
	private String windMax;
	private String hmidMin;
	private String hmidMax;
	private String wpi;
	private String wrumb;
	

	public Day() {

	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public String getCloud() {
		return cloud;
	}

	public void setCloud(String cloud) {
		this.cloud = cloud;
	}

	public String getPpcp() {
		return ppcp;
	}

	public void setPpcp(String ppcp) {
		this.ppcp = ppcp;
	}

	public String gettMin() {
		return tMin;
	}

	public void settMin(String tMin) {
		this.tMin = tMin;
	}

	public String gettMax() {
		return tMax;
	}

	public void settMax(String tMax) {
		this.tMax = tMax;
	}

	public String getpMin() {
		return pMin;
	}

	public void setpMin(String pMin) {
		this.pMin = pMin;
	}

	public String getpMax() {
		return pMax;
	}

	public void setpMax(String pMax) {
		this.pMax = pMax;
	}

	public String getWindMin() {
		return windMin;
	}

	public void setWindMin(String windMin) {
		this.windMin = windMin;
	}

	public String getWindMax() {
		return windMax;
	}

	public void setWindMax(String windMax) {
		this.windMax = windMax;
	}

	public String getHmidMin() {
		return hmidMin;
	}

	public void setHmidMin(String hmidMin) {
		this.hmidMin = hmidMin;
	}

	public String getHmidMax() {
		return hmidMax;
	}

	public void setHmidMax(String hmidMax) {
		this.hmidMax = hmidMax;
	}

	public String getWpi() {
		return wpi;
	}

	public void setWpi(String wpi) {
		this.wpi = wpi;
	}

	public String getPict() {
		return pict;
	}

	public void setPict(String pict) {
		this.pict = pict;
	}

	public String getWrumb() {
		String result="";
		if (wrumb==null) {
			return result;
		}
		int i=Integer.parseInt(wrumb);
		if (i>=0 && i<=22 ) {
			result="N";
		}
		if (i>=23 && i<=68 ) {
			result="NE";
		}
		if (i>=69 && i<=113 ) {
			result="E";
		}
		if (i>=114 && i<=158 ) {
			result="SE";
		}
		if (i>=159 && i<=204 ) {
			result="S";
		}
		if (i>=205 && i<=250 ) {
			result="SW";
		}
		if (i>=251 && i<=294 ) {
			result="W";
		}
		if (i>=295 && i<=339 ) {
			result="NW";
		}
		if (i>=340 ) {
			result="N";
		}
		return result;
	}

	public void setWrumb(String wrumb) {
		this.wrumb = wrumb;
	}

	@Override
	public String toString() {
		return "Day [date=" + date + ", hour=" + hour + ", cloud=" + cloud
				+ ", pict=" + pict + ", ppcp=" + ppcp + ", tMin=" + tMin
				+ ", tMax=" + tMax + ", pMin=" + pMin + ", pMax=" + pMax
				+ ", windMin=" + windMin + ", windMax=" + windMax
				+ ", hmidMin=" + hmidMin + ", hmidMax=" + hmidMax + ", wpi="
				+ wpi + "]";
	}
}
