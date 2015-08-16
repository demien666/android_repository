package com.demien.weather.ua;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HelpActivity extends Activity {
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		/*
		 Button btnBack;
		btnBack=(Button)findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				HelpActivity.this.finish();
				
			}
		});
		*/
	}
	
	public void btnBackOnClick(View v) {
		HelpActivity.this.finish();
	}

	
	public void showDevInfoActivity(View v) {
		Intent intent = new Intent(this, DevInfoActivity.class);
		startActivity(intent);		
	}

}
