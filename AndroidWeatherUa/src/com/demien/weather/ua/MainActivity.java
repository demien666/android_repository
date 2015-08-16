package com.demien.weather.ua;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.demien.weather.ua.domain.Day;
import com.demien.weather.ua.domain.Report;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends BaseActivity {

	MainActivityController controller = new MainActivityController(this);

	// UI controls
	private ListView lvReport;
	private SimpleAdapter adapterReport;
	private ArrayList<HashMap<String, Object>> listReport;

	private Spinner spinnerCity;
	private ArrayAdapter<String> spinnerCityAdapter;
	private TextView tvLastUpdated;

	// CONSTANTS

	private final int SEARCH_FORM_ID = 1;
	private final int REMOVE_CITY_FORM_ID = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		String tag = "MainActivity.onCreate";
		Log.i(tag, "started");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// components
		Log.i(tag, "Components init");
		spinnerCityAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item);
		spinnerCityAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerCity = (Spinner) findViewById(R.id.spinnerCity);
		spinnerCity.setAdapter(spinnerCityAdapter);

		spinnerCity.setOnItemSelectedListener(new SpinnerSelectionListener());

		lvReport = (ListView) findViewById(R.id.lvReport);
		lvReport.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				showDetailForm(position);
				// TODO Auto-generated method stub
				//showErrorMessage("Position="+position+" Id="+id);
			}
		});

		tvLastUpdated = (TextView) findViewById(R.id.tvLastUpdated);
		setLastUpdated("");

		Log.i(tag, "Controler init");
		controller = new MainActivityController(this);

		controller.loadSettings();
	}


	public void repopulateSpinner(Map<String, String> cityUrlMap) {
		spinnerCityAdapter.clear();
		// add cities to adapter
		for (String s : cityUrlMap.keySet()) {
			spinnerCityAdapter.add(s);
		}

		spinnerCity.setSelection(0);

	}

	public void setSpinnerCityEnabled(boolean enable) {
		spinnerCity.setEnabled(enable);
	}

	public void setLastUpdated(String date) {
		Log.i("setLastUpdated", date);
		if (date != null && date.length() > 0) {
			tvLastUpdated.setText(getResources().getString(
					R.string.last_updated)
					+ date);
		}
	}

	public void removeSpinnerCity(String cityName) {
		spinnerCityAdapter.remove(cityName);
	}

	public void refreshCurrentReport() {
		String tag = "refreshCurrentReport";
		Log.i(tag, "started");
		//spinnerCity.getSe
		//String currentCity = spinnerCity.getItemAtPosition(0).toString();
		String currentCity=null;
		try {
		     currentCity = spinnerCity.getSelectedItem().toString();
		} catch (Exception e ) {
			Log.e(tag, "current city was not defined");
		}
		if (currentCity!=null) {
		Log.i("refreshCurrentReport",
				"current report: executing loadCityReport for " + currentCity);
		displayCityReport(currentCity);
		}

	}

	public void displayCityReport(String cityName) {
		// do it!
		String tag = "displayCityReport";
		//Log.i(tag, "Loading report for city" + cityName);

		listReport = controller.getListReportData2(cityName);
		//Log.i(tag, "records in list:" + listReport.size());
		// set adapter
		//Log.i("displayCityReport", "Creating adapter");
		adapterReport = new SimpleAdapter(this, listReport,
				R.layout.report_line, new String[] { AppConst.REPORT_ROW_TITLE,
						AppConst.REPORT_TEMP_M, AppConst.REPORT_TEMP_D,
						AppConst.REPORT_TEMP_E, AppConst.REPORT_TEMP_N,
						//AppConst.REPORT_CLOUD_M, AppConst.REPORT_CLOUD_D,
						//AppConst.REPORT_CLOUD_E, AppConst.REPORT_CLOUD_N,
						//AppConst.REPORT_RAIN_M, AppConst.REPORT_RAIN_D,
						//AppConst.REPORT_RAIN_E, AppConst.REPORT_RAIN_N,
						AppConst.REPORT_PICT_M, AppConst.REPORT_PICT_D,
						AppConst.REPORT_PICT_E, AppConst.REPORT_PICT_N },
				new int[] { R.id.tvReportLine1, R.id.tvReportTempM,
						R.id.tvReportTempD, R.id.tvReportTempE,
						R.id.tvReportTempN, //R.id.tvReportCloudM,
						//R.id.tvReportCloudD, R.id.tvReportCloudE,
						//R.id.tvReportCloudN, R.id.tvReportRainM,
						//R.id.tvReportRainD, R.id.tvReportRainE,
						//R.id.tvReportRainN, 
						R.id.ivReportPictM,
						R.id.ivReportPictD, R.id.ivReportPictE,
						R.id.ivReportPictN });

		//Log.i("displayCityReport", "Setting adapter");
		lvReport.setAdapter(adapterReport);

	}

	public void btnExitOnClick(View v) {
		System.exit(0);
	}

	public void btnRefreshOnClick(View v) {
		controller.downloadReportData();
	}

	public void showSearchForm() {
		Intent intent = new Intent(this, SearchActivity.class);
		startActivityForResult(intent, SEARCH_FORM_ID);
	}

	public void btnAddCityOnClick(View v) {
		// Intent intent = new Intent(this, SearchActivity.class);
		// startActivityForResult(intent, 1);
		// startActivity(intent);
		showSearchForm();
	}

	class SpinnerSelectionListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// displayCityReport(spinnerCity.getItemAtPosition(position).toString());
			// refreshCurrentReport();
			String currentCity = spinnerCity.getItemAtPosition(position)
					.toString();
			Log.i("onItemSelected",
					"current report: executing loadCityReport for "
							+ currentCity);
			displayCityReport(currentCity);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		String tag = "onActivityResult";
		Log.i(tag, "processing. resultCode=" + resultCode);
		if (resultCode == RESULT_OK) {
			if (requestCode == SEARCH_FORM_ID) {
				Log.i(tag, "result from search form");
				if (data.hasExtra("cityName") && data.hasExtra("cityId")) {
					controller.processSearchResultForm(data);
					// Toast.makeText(this,
					// data.getExtras().getString("cityName"),
					// Toast.LENGTH_SHORT).show();
				} else {
					Log.w(tag, "cityName and cityId were not found in extras");
				}
			} // SEARCH_FORM_ID
			if (requestCode == REMOVE_CITY_FORM_ID) {
				Log.i(tag, "result from remove city form");
				if (data.hasExtra("cityName")) {
					controller.processRemoveCityResultForm(data);
				}
			}

		} else { // if (resultCode == RESULT_OK)
			Log.w(tag, "result code<>OK");
		}
	}

	public void showRemoveCityForm() {
		// populate current city list
		String cityList = null;
		if (!controller.getCityUrlMap().isEmpty()) {

			for (String s : controller.getCItyReportMap().keySet()) {
				if (cityList == null) {
					cityList = s;
				} else {
					cityList = cityList + AppConst.ELEMENT_SEPARATOR + s;
				}
			}
		}
		Intent intent = new Intent(this, RemoveCityActivity.class);

		if (cityList != null) {
			intent.putExtra(AppConst.CITY_LIST_TO_REMOVE_KEY, cityList);
		}
		startActivityForResult(intent, REMOVE_CITY_FORM_ID);
	}

	public void btnRemoveCityOnClick(View v) {
		showRemoveCityForm();

	}
	
	public void showDetailForm(int index) {
		String tag="showDetailForm";

		
		HashMap<String, Object> hm=listReport.get(index);
		String reportTitle=(String)hm.get(AppConst.REPORT_ROW_TITLE);
		//cut day of week
		reportTitle=reportTitle.substring(0, reportTitle.lastIndexOf(" "));
		Log.i(tag, "report title truncated="+reportTitle);
		String cityName=reportTitle.substring(0, reportTitle.lastIndexOf(" "));
		//Log.i(tag, "cityName="+cityName);
		String reportDateString=reportTitle.substring(reportTitle.lastIndexOf(" ")+1);
		//Log.i(tag, "reportDateString="+reportDateString);
		
		String currentCity = spinnerCity.getSelectedItem().toString();
		//Log.i(tag, "current city="+currentCity);
		Report report=controller.getCItyReportMap().get(currentCity);

		
		 
		StringBuilder detail=new StringBuilder();
		
		detail.append(getResources().getString(R.string.tvDetailCity)+  cityName).append(AppConst.EOL);
		detail.append(getResources().getString(R.string.tvDetailDate)+  reportDateString).append(AppConst.EOL);

		for (Day day:report.getDayReports()) {
		   	Date date= controller.stringToDate(day.getDate());
		   	String dateStr=controller.dateToClientString(date, AppConst.CLIENT_DATE_FORMAT_DAY);
		   	// cut day of week 
		   	dateStr=dateStr.substring(0, dateStr.lastIndexOf(" "));
		   	if (dateStr.equals(reportDateString)) {
		   		detail.append(AppConst.EOL);
		   		detail.append( getResources().getString(R.string.tvDetailTime)+day.getHour()+":00").append(AppConst.EOL);
		   		detail.append( getResources().getString(R.string.tvDetailTemp)+day.gettMin()+".."+day.gettMax()).append(AppConst.EOL);
		   		detail.append( getResources().getString(R.string.tvDetailCloud)+day.getCloud()).append(AppConst.EOL);
		   		detail.append( getResources().getString(R.string.tvDetailPpcp)+day.getPpcp()).append(AppConst.EOL);
		   		detail.append( getResources().getString(R.string.tvDetailWind)+day.getWindMin()+".."+day.getWindMax()).append(AppConst.EOL);
		   		detail.append( getResources().getString(R.string.tvDetailWindDirection)+day.getWrumb()).append(AppConst.EOL);
		   		detail.append( getResources().getString(R.string.tvDetailPressure)+day.getpMin()+".."+day.getpMax()).append(AppConst.EOL);
		   		detail.append( getResources().getString(R.string.tvDetailHmid)+day.getHmidMin()+".."+day.getHmidMin()).append(AppConst.EOL);
		   	}
		}		
		
		//Log.i(tag, "preparing to start DetailActivity");
		Intent intent = new Intent(this, DetailActivity.class);
	    intent.putExtra(AppConst.DATAIL_DATA_KEY, detail.toString());
		//intent.putExtra(AppConst.DATAIL_DATA_KEY, "hello world!");
		//Log.i(tag, "starting DetailActivity");
		startActivity(intent);
	}


	public void btnHelpOnClick(View v) {
		Intent intent = new Intent(this, HelpActivity.class);
		startActivity(intent);
	}

}
