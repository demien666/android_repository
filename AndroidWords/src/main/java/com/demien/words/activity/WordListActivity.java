package com.demien.words.activity;

import android.app.Activity;
import android.os.Bundle;
import com.demien.words.R;

/**
 * Created by dmitry on 10.05.15.
 */
public class WordListActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list_activity);
    }
}