package com.demien.browser;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    /**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized after 
     * previously being shut down then this Bundle contains the data it most 
     * recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */

    TextView tvAddress;
    WebView wvMain;
    GestureListener gestureListener;
    GestureDetector gestureDetector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvAddress=(TextView)findViewById(R.id.tvAddress);
        wvMain=(WebView)findViewById(R.id.wvMain);
        wvMain.getSettings().setJavaScriptEnabled(true);
        wvMain.getSettings().setBuiltInZoomControls(true);


        wvMain.setWebViewClient(new CallBack());

        gestureListener=new GestureListener();
        gestureDetector= new GestureDetector(this, gestureListener);



        //RelativeLayout relativeLayout=(RelativeLayout)findViewById(R.id.layoutMain);
        //relativeLayout.setOnTouchListener(new View.OnTouchListener() {

        wvMain.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionEvent) {
                wvMain.onTouchEvent(motionEvent);
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });

        //wvMain.setWebChromeClient(new CallBack());

        wvMain.getSettings().setUseWideViewPort(true);
        wvMain.getSettings().setLoadWithOverviewMode(false);



        //wvMain.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.demien.browser.R.menu.main, menu);
	return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(event.getAction() == KeyEvent.ACTION_DOWN){
            switch(keyCode)
            {
                case KeyEvent.KEYCODE_BACK:
                    if(wvMain.canGoBack() == true){
                        wvMain.goBack();
                    }else{
                        finish();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void goToURL(String url) {
        if (!url.contains("http://")) {
            url="http://"+url;
        }

        wvMain.loadUrl(url);
    }


    public void btnGo_onClick(View v) {
        String url=tvAddress.getText().toString();
        goToURL(url);

    }

    public class CallBack extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            goToURL(url);
            return true;

        }
    }

}

