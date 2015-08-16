package com.demien.weather.ua;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class DevInfoActivity extends Activity {
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dev_info);
		/*
		Button btnDevInfoBack;
		btnDevInfoBack=(Button)findViewById(R.id.btnDevInfoBack);
		btnDevInfoBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DevInfoActivity.this.finish();
				
			}
		});
		*/
	}
	
	public void btnBackOnClick(View v) {
		DevInfoActivity.this.finish();
	}


}
