package com.demien.weather.ua;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import android.content.Intent;
import android.util.Log;

import com.demien.weather.ua.domain.Day;
import com.demien.weather.ua.domain.Report;
import com.demien.weather.ua.parser.WUAParser;
import com.demien.weather.ua.service.RestClient.RequestMethod;
import com.demien.weather.ua.service.RestClientAsync;
import com.demien.weather.ua.service.RestClientResult;
import com.demien.weather.ua.service.SettingsHelper;

public class MainActivityController {
	private MainActivity view;

	// settings
	private SettingsHelper settingsHelper;
	private final String CITY_URL_MAP_KEY = "CITY_URL_MAP";
	private final String CITY_REPORT_MAP_KEY = "CITY_REPORT_MAP";
	private final String LAST_UPDATED_KEY = "LAST_UPDATED";

	private Map<String, String> cityUrlMap = new HashMap<String, String>();
	private Map<String, Report> cityReportMap = new HashMap<String, Report>();
	private final Map<String, String> dayTimeMap = new HashMap<String, String>();
	private String lastUpdated = "";

	public MainActivityController(MainActivity view) {
		String tag = "MainActivityController.Constructor";
		Log.i(tag, "Started");
		this.view = view;

	}

	public Map<String, String> getCityUrlMap() {
		return cityUrlMap;
	}

	public Map<String, Report> getCItyReportMap() {
		return cityReportMap;
	}

	@SuppressWarnings("unchecked")
	public void loadSettings() {
		String tag = "loadSettings";
		settingsHelper = new SettingsHelper(view);
		// get sity
		try {
			Log.i(tag, "trying to load cityUrlMap");
			cityUrlMap = (HashMap<String, String>) settingsHelper
					.getObject(CITY_URL_MAP_KEY);
			Log.i(tag, "cityUrlMap loaded!");
		} catch (Exception e) {
			Log.e(tag, "trying to load cityUrlMap ERROR:" + e.getMessage());
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		if (cityUrlMap != null && !cityUrlMap.isEmpty()) {
			try {
				Log.i(tag, "trying to load cityReportMap");
				cityReportMap = (HashMap<String, Report>) settingsHelper
						.getObject(CITY_REPORT_MAP_KEY);
				Log.i(tag, "cityReportMap loaded!");
				lastUpdated = settingsHelper.getValue(LAST_UPDATED_KEY);
				Log.i(tag, "lastUpdated loaded!:" + lastUpdated);
			} catch (Exception e) {
				Log.e(tag,
						"trying to load cityReportMap ERROR:" + e.getMessage());
			}
			// load cities setup
			// cityUrlMap.put("Kiev","http://xml.weather.co.ua/1.2/forecast/23?dayf=5&lang=ru&userid=yoursite_com");

			filterReportsByCurrentDate();

			view.repopulateSpinner(cityUrlMap);
			view.setLastUpdated(lastUpdated);
			repopulateDateTimeMap();

			view.refreshCurrentReport();
		} else {
			view.setSpinnerCityEnabled(false);
			//view.showSearchForm();
			view.btnHelpOnClick(null);
		}

	}

	public void repopulateDateTimeMap() {
		dayTimeMap.put("3",
				view.getResources().getString(R.string.day_time_night));
		dayTimeMap.put("9",
				view.getResources().getString(R.string.day_time_mourning));
		dayTimeMap.put("15",
				view.getResources().getString(R.string.day_time_day));
		dayTimeMap.put("21",
				view.getResources().getString(R.string.day_time_evening));
	}

	public void saveSettings() {
		String tag = "saveSettings";
		Log.i(tag, "saving settings");
		if (!cityUrlMap.isEmpty()) {
			try {
				//Log.i(tag, "saving cityUrlMap");
				settingsHelper.putObject(CITY_URL_MAP_KEY, cityUrlMap);
				//Log.i(tag, "saved!");
			} catch (Exception e) {
				Log.e(tag, "Error in saving cityUrlMap:" + e.getMessage());
				// e.printStackTrace();
			}
		}

		if (!cityReportMap.isEmpty()) {
			try {
				//Log.i(tag, "saving cityReportMap");
				settingsHelper.putObject(CITY_REPORT_MAP_KEY, cityReportMap);
				//Log.i(tag, "saved!");
				//Log.i(tag, "saving lastUpdated");
				settingsHelper.putValue(LAST_UPDATED_KEY, lastUpdated);
				//Log.i(tag, "saved!:" + lastUpdated);
			} catch (Exception e) {
				Log.e(tag, "Error in saving cityReportMap:" + e.getMessage());
				// e.printStackTrace();
			}
		}
	}

	public void downloadReportData() {
		String tag = "downloadReportData";
		view.setLastUpdated( view.getResources().getString(R.string.updating));
		try {
			// RestClient restClient = new RestClient();

			for (String city : cityUrlMap.keySet()) {
				// 1 get data
				String url = cityUrlMap.get(city);
				Log.i(tag, "Executing REST Client request for " + city + ":"
						+ url);
				RestClientAsync restClient = new RestClientAsync(
						RequestMethod.GET, url);
				ExecutorService executor = new ScheduledThreadPoolExecutor(2);

				Future<RestClientResult> futureResult = executor
						.submit(restClient);
				RestClientResult result = futureResult.get();

				// restClient.Execute(RequestMethod.GET, url, null, null);
				if (result.getErrorMessage() != null) {
					String msg = "Error downloading report for " + city + ":"
							+ result.getErrorMessage();
					Log.e(tag, msg);
					view.showErrorMessage(msg);
				} else {
					String data = result.getResponse();
					// 2 parse data
					Log.i(tag, "Parsing response data");
					WUAParser parser = new WUAParser(data);
					Report report = parser.parse();
					for (Day d:report.getDayReports()) {
						Log.i(tag, "dates:"+d.toString());
					}
					cityReportMap.put(city, report);
					lastUpdated = dateToClientString(new Date(), AppConst.CLIENT_DATE_FORMAT_HOUR);
					view.setLastUpdated(lastUpdated);
				}

			}
			saveSettings();
			// 3 update view
			view.refreshCurrentReport();
			/*
			 * String city = "UNKNOWN"; for (String s : cityUrlMap.keySet()) {
			 * city = s; } displayCityReport(city);
			 */
		} catch (Exception e) {
			String message = "Error in REFRESH procedure:" + e.getMessage();
			if (e.getCause()!=null) {
				message=message+" CAUSE="+e.getCause().getMessage();
			}

			Log.e("btnRefreshOnClick", message);
			view.showErrorMessage(message);
		}

	}

	private List<String> getDatesList(List<Day> days) {
		List<String> result = new ArrayList<String>();
		for (Day d : days) {
			String date = d.getDate();
			if (!result.contains(date)) {
				result.add(date);
			}
		}
		return result;
	}

	public ArrayList<HashMap<String, Object>> getListReportData(String cityName) {
		Report report = cityReportMap.get(cityName);
		ArrayList<HashMap<String, Object>> listReport;

		if (report == null) {
			view.showErrorMessage("Report for city " + cityName
					+ " was not found. Try to refresh data.");
			return null;
		}

		listReport = new ArrayList<HashMap<String, Object>>();

		HashMap<String, Object> hm;
		listReport.clear();
		Log.i("displayCityReport", "Count of days:"
				+ report.getDayReports().size());
		for (Day day : report.getDayReports()) {

			hm = new HashMap<String, Object>();
			// hm.put(REPORT_ROW_TITLE, report.getCityName() + " " +
			// day.getDate() + " " + day.getHour() + " cloud:" + day.getCloud()
			// + "% rain:" + day.getPpcp() + "%");
			hm.put(AppConst.REPORT_ROW_TITLE,
					report.getCityName() + " "
							+ stringToTextDate(day.getDate()) + " "
							+ dayTimeMap.get(day.getHour()) + " cloud:"
							+ day.getCloud() + "% rain:" + day.getPpcp() + "%");
			/*
			 * hm.put(AppConst.REPORT_ROW_DATA, "T:" + day.gettMin() + ".." +
			 * day.gettMax() + " P:" + day.getpMin() + ".." + day.getpMax() +
			 * " Wind:" + day.getWindMin() + ".." + day.getWindMax() + " Hmid:"
			 * + day.getHmidMin() + ".." + day.getHmidMax());
			 */
			// hm.put(AppConst.REPORT_TEMP_M, value);
			listReport.add(hm);
		}
		return listReport;
	}

	public ArrayList<HashMap<String, Object>> getListReportData2(String cityName) {
		String tag = "getListReportData2";
		Report report = cityReportMap.get(cityName);
		ArrayList<HashMap<String, Object>> result;

		if (report == null) {
			view.showErrorMessage("Report for city " + cityName
					+ " was not found. Try to refresh data.");
			return null;
		}

		result = new ArrayList<HashMap<String, Object>>();

		HashMap<String, Object> hm;
		result.clear();
		List<String> dates = getDatesList(report.getDayReports());
		Log.i(tag, "Count of distict days:" + dates.size());

		for (String date : dates) {
			hm = new HashMap<String, Object>();

			String[] tempArr = new String[25];
			String[] cloudArr = new String[25];
			String[] rainArr = new String[25];
			String[] pictArr = new String[25];
			for (Day day : report.getDayReports()) {
				if (day.getDate().equals(date)) {
					int index = Integer.parseInt(day.getHour());

					String temp = day.gettMin() + ".." + day.gettMax();
					tempArr[index] = temp;

					String cloud = day.getCloud() + "%";
					cloudArr[index] = new String(cloud);

					String rain = day.getPpcp() + "%";
					rainArr[index] = rain;
					
					String pict=day.getPict();
					pictArr[index]=pict;
					//Log.i(tag, "added pict:"+pict+" for index:"+index);

				}
			}
			//
			/*
			 * Log.i(tag, "found temperatures for date " + date + ": N:" +
			 * tempArr[AppConst.HOUR_NIGHT] + ",M:" +
			 * tempArr[AppConst.HOUR_MORNING] + ",D:" +
			 * tempArr[AppConst.HOUR_DAY] + ",E:" +
			 * tempArr[AppConst.HOUR_EVENING]);
			 */
			hm.put(AppConst.REPORT_ROW_TITLE, report.getCityName() + " "
					+ stringToTextDate(date));

			hm.put(AppConst.REPORT_TEMP_M, tempArr[AppConst.HOUR_MORNING]);
			hm.put(AppConst.REPORT_TEMP_D, tempArr[AppConst.HOUR_DAY]);
			hm.put(AppConst.REPORT_TEMP_E, tempArr[AppConst.HOUR_EVENING]);
			hm.put(AppConst.REPORT_TEMP_N, tempArr[AppConst.HOUR_NIGHT]);

			//hm.put(AppConst.REPORT_CLOUD_M, cloudArr[AppConst.HOUR_MORNING]);
			//hm.put(AppConst.REPORT_CLOUD_D, cloudArr[AppConst.HOUR_DAY]);
			//hm.put(AppConst.REPORT_CLOUD_E, cloudArr[AppConst.HOUR_EVENING]);
			//hm.put(AppConst.REPORT_CLOUD_N, cloudArr[AppConst.HOUR_NIGHT]);

			//hm.put(AppConst.REPORT_RAIN_M, rainArr[AppConst.HOUR_MORNING]);
			//hm.put(AppConst.REPORT_RAIN_D, rainArr[AppConst.HOUR_DAY]);
			//hm.put(AppConst.REPORT_RAIN_E, rainArr[AppConst.HOUR_EVENING]);
			//hm.put(AppConst.REPORT_RAIN_N, rainArr[AppConst.HOUR_NIGHT]);
			
			// pict
			hm.put(AppConst.REPORT_PICT_M, getPict(pictArr[AppConst.HOUR_MORNING]));
			hm.put(AppConst.REPORT_PICT_D, getPict(pictArr[AppConst.HOUR_DAY]));
			hm.put(AppConst.REPORT_PICT_E, getPict(pictArr[AppConst.HOUR_EVENING]));
			hm.put(AppConst.REPORT_PICT_N, getPict(pictArr[AppConst.HOUR_NIGHT]));

			result.add(hm);
		}

		return result;
	}
	
	public Object getPict(String value) {
		Object result=R.drawable.na_255;
		if (value==null) {
			return result;
		}
		if (value.equals("_0_sun.gif")) {
			result= R.drawable.sun_0;
		}
		if (value.equals("_0_moon.gif")) {
			result= R.drawable.moon_0;
		}
		if (value.equals("_1_sun_cl.gif")) {
			result= R.drawable.suncl_1;
		}
		if (value.equals("_1_moon_cl.gif")) {
			result= R.drawable.mooncl_1;
		}
		if (value.equals("_2_cloudy.gif")) {
			result= R.drawable.cloudy_2;
		}
		if (value.equals("_3_pasmurno.gif")) {
			result= R.drawable.pasmurno_3;
		}
		if (value.equals("_4_short_rain.gif")) {
			result= R.drawable.shortrain4;
		}
		if (value.equals("_5_rain.gif")) {
			result= R.drawable.rain5;
		}
		if (value.equals("_6_lightning.gif")) {
			result=R.drawable.lightning_6;
		}
		if (value.equals("_7_hail.gif")) {
			result= R.drawable.hail_7;
		}
		if (value.equals("_8_rain_snow.gif")) {
			result=R.drawable.rainsnow_8;
		}
		if (value.equals("_9_show.gif")) {
			result= R.drawable.snow_9;
		}
		if (value.equals("_10_heavy_show.gif")) {
			result= R.drawable.heavysnow_10;
		}
				
		
		return result;
	}

	public Date stringToDate(String dateStr) {
		Date result = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(
					AppConst.SERVER_DATE_FORMAT, Locale.getDefault());
			result = formatter.parse(dateStr);
		} catch (Exception e) {
			Log.e("stringToDate", e.getMessage());
		}
		return result;
	}

	public String dateToClientString(Date d, String format) {
		DateFormat fmt = new SimpleDateFormat(format,
				Locale.getDefault());
		String result = fmt.format(d);
		return result;
	}

	public String stringToTextDate(String dateStr) {
		Date d = stringToDate(dateStr);
		return dateToClientString(d, AppConst.CLIENT_DATE_FORMAT_DAY);
	}

	public void processSearchResultForm(Intent data) {
		String tag = "processSearchResultForm";
		String cityName = data.getExtras().getString("cityName");
		String cityId = data.getExtras().getString("cityId");
		/*
		String cityUrl = "http://xml.weather.co.ua/1.2/forecast/" + cityId
				+ "?dayf=5&lang=ru&userid=yoursite_com"; */
		String cityUrl=AppConst.URL_CITY.replace(AppConst.PATTERN_CITY_ID, cityId);
		cityUrl=cityUrl.replace(AppConst.PATTERN_LANG, view.getLang());
		Log.i(tag, "cityName=" + cityName + " cityUrl=" + cityUrl);
		cityUrlMap.put(cityName, cityUrl);
		view.setSpinnerCityEnabled(true);

		view.repopulateSpinner(cityUrlMap);
		saveSettings();
		downloadReportData();
		loadSettings();

	}

	public void processRemoveCityResultForm(Intent data) {
		String cityName = data.getExtras().getString("cityName");
		cityUrlMap.remove(cityName);
		cityReportMap.remove(cityName);
		saveSettings();
		// spinner remove
		view.removeSpinnerCity(cityName);
		view.refreshCurrentReport();
		// spinnerCityAdapter.remove(cityName);
	}

	public Date truncateTime(Date d) throws ParseException {
		String tag="truncateTime";
		Log.i(tag, "Date for truncate:"+d.toString());
		DateFormat formatter = new SimpleDateFormat(AppConst.SERVER_DATE_FORMAT, Locale.getDefault());
		String s = formatter.format(d);

		Date result = formatter.parse(s);
		
		Log.i(tag, "Truncated:"+result.toString());

		return result;
	}

	public void filterReportsByCurrentDate() {
		String tag = "filterReportsByCurrentDate";
		try {
			
			Map<String, Report> newMap = new HashMap<String, Report>();
			Date currentDate = truncateTime(new Date());
			Log.i(tag, "currentDay:"+currentDate);
			for (String c : cityReportMap.keySet()) {
				Report report = cityReportMap.get(c);
				Report newReport = report.clone();
				for (Day d : report.getDayReports()) {
					if (stringToDate(d.getDate()).getTime() >= currentDate
							.getTime() ) { 
						newReport.addDayReport(d);
						// Log.i(tag, "removed:" + d.toString());
					} else {
						Log.i(tag, "excluded:"+d.getDate());
					}
				}
				newMap.put(c, newReport);
			}
			cityReportMap = newMap;
		} catch (Exception e) {
			Log.e(tag, e.getMessage());

		}
	}

}
