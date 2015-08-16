package com.demien.lessons;

import android.app.*;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import javafx.scene.Scene;

import java.io.IOException;
import java.io.InputStream;

public class HelloAndroidActivity extends Activity {

    final String LOG_TAG = "myLogs";

    final Uri CONTACT_URI = Uri
            .parse("content://ru.startandroid.providers.AdressBook/contacts");

    final String CONTACT_NAME = "name";
    final String CONTACT_EMAIL = "email";

    Scene mAScene;
    Scene mAnotherScene;



    /**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized after 
     * previously being shut down then this Bundle contains the data it most 
     * recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


// Create the scene root for the scenes in this app
        //mSceneRoot = (ViewGroup) findViewById(R.id.scene_root);

// Create the scenes
        //mAScene = Scene.getSceneForLayout(mSceneRoot, R.layout.a_scene, this);
        //mAnotherScene =
         //       Scene.getSceneForLayout(mSceneRoot, R.layout.another_scene, this);

    }

    public void onDoSomeAction(View v) {

    }


    public void createContentProvider() {
        Cursor cursor = getContentResolver().query(CONTACT_URI, null, null,
                null, null);
        startManagingCursor(cursor);

        String from[] = { "name", "email" };
        int to[] = { android.R.id.text1, android.R.id.text2 };
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2, cursor, from, to);

        ListView lvContact = (ListView) findViewById(R.id.lvContact);
        lvContact.setAdapter(adapter);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "Landscape", Toast.LENGTH_SHORT);
        } else {
            Toast.makeText(this, "Portret", Toast.LENGTH_SHORT);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(com.demien.lessons.R.menu.main, menu);
	return true;
    }

    public String loadText(String textFileName) {
        AssetManager assetManager=getAssets();
        InputStream is=null;
        try {
            is=assetManager.open(textFileName);
            String text=convertStreamToString(is);
            return text;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public void onClickInsert(View v) {
        /*
        ContentValues cv = new ContentValues();
        cv.put(CONTACT_NAME, "name 4");
        cv.put(CONTACT_EMAIL, "email 4");
        Uri newUri = getContentResolver().insert(CONTACT_URI, cv);
        Log.d(LOG_TAG, "insert, result Uri : " + newUri.toString());
        */
        Intent intent = new Intent(this, HelloAndroidActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

// build notification
// the addAction re-use the same intent to keep the example short
        Notification n  = new Notification.Builder(this)
                .setContentTitle("New mail from " + "test@gmail.com")
                .setContentText("Subject")
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_launcher, "Call", pIntent)
                .addAction(R.drawable.ic_launcher, "More", pIntent)
                .addAction(R.drawable.ic_launcher, "And more", pIntent)
                .build();


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, n);

    }

    public void onClickUpdate(View v) {
        ContentValues cv = new ContentValues();
        cv.put(CONTACT_NAME, "name 5");
        cv.put(CONTACT_EMAIL, "email 5");
        Uri uri = ContentUris.withAppendedId(CONTACT_URI, 2);
        int cnt = getContentResolver().update(uri, cv, null, null);
        Log.d(LOG_TAG, "update, count = " + cnt);
    }

    public void onClickDelete(View v) {
        Uri uri = ContentUris.withAppendedId(CONTACT_URI, 3);
        int cnt = getContentResolver().delete(uri, null, null);
        Log.d(LOG_TAG, "delete, count = " + cnt);
    }

    public void onClickError(View v) {
        Uri uri = Uri.parse("content://ru.startandroid.providers.AdressBook/phones");
        try {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        } catch (Exception ex) {
            Log.d(LOG_TAG, "Error: " + ex.getClass() + ", " + ex.getMessage());
        }

    }

}

