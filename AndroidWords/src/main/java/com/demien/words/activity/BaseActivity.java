package com.demien.words.activity;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by dmitry on 21.02.15.
 */
public class BaseActivity extends Activity {

    public String getResourceString(int id) {
        return getResources().getString(id);
    }

    public StringBuilder getResourceStringBuilder(int id) {
        return new StringBuilder(getResourceString(id));
    }

    public void finishWithResult(String param, String value) {
        Intent data=new Intent();
        data.putExtra(param, value);
        setResult(RESULT_OK, data);
        finish();
    }

    public void showActivity(Class<?> cl, int formId ) {
        Intent intent=new Intent(this, cl);
        startActivityForResult(intent, formId);
    }
}
