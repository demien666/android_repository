package com.demien.weather.ua;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import com.demien.weather.ua.domain.SearchResult;
import com.demien.weather.ua.parser.WUASearchParser;
import com.demien.weather.ua.service.RestClient.RequestMethod;
import com.demien.weather.ua.service.RestClientAsync;
import com.demien.weather.ua.service.RestClientResult;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class SearchActivity extends BaseActivity {
	// ui components
	private EditText edtCityName;
	private ListView lvSearchResult;

	// data
	private List<SearchResult> searchResultList;

	// private ArrayList<HashMap<String, Object>> searchResulAdapterData;

	// constants
	// private final String SEARCH_RESULT_CITY_NAME = "name";
	// private final String SEARCH_RESULT_CHECKBOX = "checkbox";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		edtCityName = (EditText) findViewById(R.id.edtCityName);
		lvSearchResult = (ListView) findViewById(R.id.lvSearchResult);
		lvSearchResult.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

		lvSearchResult.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// showErrorMessage("Position="+arg2);
				Intent data = new Intent();
				SearchResult result = searchResultList.get(arg2);
				data.putExtra("cityId", result.getCityId());
				data.putExtra("cityName", result.getCityName());
				data.putExtra("region", result.getRegion());
				data.putExtra("country", result.getCountry());

				// Activity finished ok, return the data
				setResult(RESULT_OK, data);
				finish();
			}
		});

	}

	public void btnSearchCityClick(View v) {
		String tag = "btnSearchCityClick";
		searchResultList = null;

		String s = edtCityName.getText().toString();
		if (s.length() < 3) {
			String msg = getResources().getString(R.string.enter3symbols);
			showErrorMessage(msg);
			return;
		}
		s = s.replace(" ", "+");

		// search
		Log.i(tag, "Starting RestClientAsync");
		String url = new String(AppConst.URL_SEARCH);
		url = url.replace(AppConst.PATTERN_LANG, getLang());
		url = url.replace(AppConst.PATTERN_CITY, s);

		// RestClientAsync search = new RestClientAsync(RequestMethod.GET,
		// AppConst.URL_SEARCH + s);
		RestClientAsync search = new RestClientAsync(RequestMethod.GET, url);
		ExecutorService executor = new ScheduledThreadPoolExecutor(2);

		Future<RestClientResult> futureResult = executor.submit(search);

		try {
			RestClientResult result = futureResult.get();
			if (result.getErrorMessage() != null) {
				showErrorMessage(result.getErrorMessage());
			} else {
				// 2. parse
				Log.i(tag, "Parsing");
				String response = "";
				try {
					response = result.getResponse();
				} catch (Exception e) {
					Log.w(tag, "Result is null :(");
				}
				if (response.length() < 1) {
					String msg = getResources().getString(R.string.notFound);
					showErrorMessage(msg);
					return;
				}
				WUASearchParser parser = new WUASearchParser(response);

				// 3. result processing
				searchResultList = parser.parse();
				if (searchResultList.size() == 0)
					return;

				Log.i(tag, "result processing");
				// searchResulAdapterData = new ArrayList<HashMap<String,
				// Object>>();

				String[] adapterData = new String[searchResultList.size()];

				for (int i = 0; i < searchResultList.size(); i++) {
					adapterData[i] = new String();
					adapterData[i] = searchResultList.get(i).toString();
				}

				// 4. set up list view
				Log.i(tag, "set up adapter");
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_single_choice,
						adapterData);
				lvSearchResult.setAdapter(adapter);
			}
		} catch (Exception e) {
			Log.e(tag, e.getMessage());
			showErrorMessage(tag + " ERROR:" + e.getMessage());
		}

		// search.run();
		/*
		 * RestClient search = new RestClient(); try { // 1. execute service
		 * Log.i("btnSearchCityClick", "Executing request");
		 * search.Execute(RequestMethod.GET, AppConst.URL_SEARCH + s, null,
		 * null);
		 * 
		 * } catch (Throwable e) {
		 * showErrorMessage("Error in btnSearchCityClick" + e.getMessage() +
		 * " CAUSE:" + e.getCause().getMessage()); }
		 */
	}

}
