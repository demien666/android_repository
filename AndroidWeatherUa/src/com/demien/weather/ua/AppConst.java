package com.demien.weather.ua;

public class AppConst {

	public final static String PATTERN_LANG="%LANG%";
	public final static String PATTERN_CITY="%CITY%";
	public final static String PATTERN_CITY_ID="%CID%";
	public final static String URL_SEARCH="http://xml.weather.co.ua/1.2/city/?search=%CITY%&lang=%LANG%";
	public final static String ELEMENT_SEPARATOR="#";
	public final static String EOL="\n";
	public final static String URL_CITY = "http://xml.weather.co.ua/1.2/forecast/%CID%?dayf=5&lang=%LANG%&userid=android_application";
	
	// remove city activity
	public final static String CITY_LIST_TO_REMOVE_KEY="CITY_LIST_TO_REMOVE";
	
	// detail activity 
	public final static String DATAIL_DATA_KEY="DETAIL_DATA_KEY";
	
	public final static String SERVER_DATE_FORMAT="yyyy-MM-dd";
	public final static String CLIENT_DATE_FORMAT_DAY="dd.MM.yyyy EEE";
	public final static String CLIENT_DATE_FORMAT_HOUR="dd.MM.yyyy HH:mm:ss";
	
	public final static int HOUR_MORNING=9;
	public final static int HOUR_DAY=15;
	public final static int HOUR_EVENING=21;
	public final static int HOUR_NIGHT=3;
	
	public final static String REPORT_ROW_TITLE = "TITLE";
	public final static String REPORT_TEMP_M = "REPORT_TEMP_M";
	public final static String REPORT_TEMP_D = "REPORT_TEMP_D";
	public final static String REPORT_TEMP_E = "REPORT_TEMP_E";
	public final static String REPORT_TEMP_N = "REPORT_TEMP_N";
	
	public final static String REPORT_CLOUD_M = "REPORT_CLOUD_M";
	public final static String REPORT_CLOUD_D = "REPORT_CLOUD_D";
	public final static String REPORT_CLOUD_E = "REPORT_CLOUD_E";
	public final static String REPORT_CLOUD_N = "REPORT_CLOUD_N";
	
	public final static String REPORT_RAIN_M = "REPORT_RAIN_M";
	public final static String REPORT_RAIN_D = "REPORT_RAIN_D";
	public final static String REPORT_RAIN_E = "REPORT_RAIN_E";
	public final static String REPORT_RAIN_N = "REPORT_RAIN_N";	
	
	public final static String REPORT_PICT_M = "REPORT_PICT_M";
	public final static String REPORT_PICT_D = "REPORT_PICT_D";
	public final static String REPORT_PICT_E = "REPORT_PICT_E";
	public final static String REPORT_PICT_N = "REPORT_PICT_N";	
}
