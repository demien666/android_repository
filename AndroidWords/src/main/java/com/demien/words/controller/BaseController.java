package com.demien.words.controller;

import android.app.Activity;
import com.demien.words.AppConst;
import com.demien.words.activity.BaseActivity;
import com.demien.words.util.SettingsHelper;

/**
 * Created by dmitry on 02.03.15.
 */
public class BaseController {

    protected SettingsHelper settings;
    protected BaseActivity activity;

    public BaseController(BaseActivity activity) {
        settings = new SettingsHelper(activity, AppConst.APP_ID);
        this.activity = activity;
    }

    public SettingsHelper getSettings() {
        return settings;
    }

}
