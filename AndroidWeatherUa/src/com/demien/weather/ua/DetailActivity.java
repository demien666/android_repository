package com.demien.weather.ua;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DetailActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		String tag="onCreate";
		Log.i(tag,"started");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		//Log.i(tag,"getting intent data");
		Intent intent = getIntent();
		String detailData = intent
				.getStringExtra(AppConst.DATAIL_DATA_KEY);
		//Log.i(tag,"got data:"+detailData);
		TextView tvDetailData=(TextView)findViewById(R.id.tvDetailData);
		tvDetailData.setText(detailData);
		/*
		Button btnDevInfoBack=(Button)findViewById(R.id.btnBackDetail);
		btnDevInfoBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DetailActivity.this.finish();
				
			}
		});
		*/
	}
	
	public void btnBackOnClick(View v) {
		DetailActivity.this.finish();
	}
}
