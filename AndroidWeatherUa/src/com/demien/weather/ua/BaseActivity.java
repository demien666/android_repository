package com.demien.weather.ua;

import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public abstract class BaseActivity extends Activity {
	public void showErrorMessage(String message) {
		Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.help, menu);
		return false;
	}
	
	public String getLang() {
		String tag="getLang";
		
		String result="en";
		String locale = java.util.Locale.getDefault().getDisplayName();
		Log.i(tag, "locale="+locale);
		if (locale.toUpperCase().indexOf("РУССК")>-1) {
			result="ru";
		}
		if (locale.toUpperCase().indexOf("УКРА")>-1) {
			result="uk";
		}
		Log.i(tag, "Lang="+result);
		
		return result;
	}
}
