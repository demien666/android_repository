package com.demien.words.util;

//import android.app.AlertDialog;
//import android.content.Context;


import android.app.AlertDialog;
import android.content.Context;
import com.demien.words.R;
import com.demien.words.activity.BaseActivity;

/**
 * Created by dmitry on 28.02.15.
 */
public class DialogHelper {

    private static void showMessage(Context context, String message, String title, int icon) {
        new AlertDialog.Builder(context)
                .setIcon(icon)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", null)
                .show();
    }

    public static void showErrorMessage(BaseActivity activity, String message) {
        showMessage(activity, message, activity.getResources().getString(R.string.const_error), android.R.drawable.ic_dialog_alert);
    }

    public static void showErrorMessage(BaseActivity activity, int messageId) {
        showMessage(activity, activity.getResourceString(messageId), activity.getResources().getString(R.string.const_error), android.R.drawable.ic_dialog_alert);
    }

    public static void showInfoMessage(BaseActivity activity, String message) {
        showMessage(activity, message, activity.getResources().getString(R.string.const_info), android.R.drawable.ic_dialog_info);
    }



}
