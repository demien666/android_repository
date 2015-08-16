package com.demien.weather.ua;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RemoveCityActivity extends BaseActivity {

	private ListView lvRemoveCity;
	private String[] cityArray; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remove_city);

		lvRemoveCity = (ListView) findViewById(R.id.lvRemoveCity);

		Intent intent = getIntent();
		String cityList = intent
				.getStringExtra(AppConst.CITY_LIST_TO_REMOVE_KEY);
		
		Log.i("onCreate", "City list:"+cityList);

		 cityArray= cityList.split(AppConst.ELEMENT_SEPARATOR);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_single_choice, cityArray);
		lvRemoveCity.setAdapter(adapter);
		
		lvRemoveCity.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent data = new Intent();
				
				data.putExtra("cityName", cityArray[arg2]);
				
				Log.i("onItemClick", "selected from removal:"+cityArray[arg2]);

				
				// Activity finished ok, return the data
				setResult(RESULT_OK, data);
				finish();
				
			}
		});
	}


}
