package com.demien.words.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import com.demien.words.R;
import com.demien.words.AppConst;
import com.demien.words.controller.LearnController;
import com.demien.words.domain.Word;
import com.demien.words.exception.LearnControllerException;
import com.demien.words.util.DialogHelper;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dmitry on 14.02.15.
 */
public class LearnActivity extends BaseActivity {

    // Constants
    private final int FORM_ID_TEST = 1;
    private final int FORM_ID_INFO = 2;
    private final int FORM_ID_SETTINGS = 3;

    // Controller
    private LearnController controller;

    // UI
    //private TextView tvLearnCaption;
    // info first raw
    private Button btnLearnInfoWDB;
    private Button btnLearnInfoWIL;
    private Button btnLearnInfoCL;
    private Button btnLearnInfoCP;

    private Button btnLearnCurrentWord;
    private Button btnLearnCurrentTranslation;
    private Button btnLearnCurrentExample;


    //private TextView tvLearnCurrentWord;
    //private TextView tvLearnCurrentTranslation;
    //private TextView tvLearnCurrentExample;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.learn_activity_layout);
        controller = new LearnController(this);

        //tvLearnCaption = (TextView) findViewById(R.id.tvLearnCaption);
        btnLearnInfoWDB = (Button) findViewById(R.id.btnLearnInfoWDB);
        btnLearnInfoWIL = (Button) findViewById(R.id.btnLearnInfoWIL);
        btnLearnInfoCL = (Button) findViewById(R.id.btnLearnInfoCL);
        btnLearnInfoCP = (Button) findViewById(R.id.btnLearnInfoCP);

        btnLearnCurrentWord= (Button) findViewById(R.id.btnLearnCurrentWord);
        btnLearnCurrentTranslation= (Button) findViewById(R.id.btnLearnCurrentTranslation);
        btnLearnCurrentExample=(Button) findViewById(R.id.btnLearnCurrentExample);


        //tvLearnCurrentWord = (TextView) findViewById(R.id.tvLearnCurrentWord);
        //tvLearnCurrentTranslation = (TextView) findViewById(R.id.tvLearnCurrentTranslation);
        //tvLearnCurrentExample = (TextView) findViewById(R.id.tvLearnCurrentExample);

        int lessonNumber=controller.getSettings().getIntValue(AppConst.SETTINGS_CURRENT_LESSON_NUMBER, 1);
        controller.moveToLessonWithNumber(lessonNumber);


        updateCurrentWord();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.demien.words.R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            /*
            case R.id.action_addNote:
                showAddNoteForm();
                return true;
            case R.id.action_info:
                showInfoForm();
                return true;
            case R.id.action_settings:
                showSettingsForm();
                return true;
            case R.id.action_exit:
                System.exit(0);
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void replace(StringBuilder builder, String pattern, String value) {
        int position = builder.indexOf(pattern);
        if (position >= 0) {
            builder.replace(position, position + pattern.length(), value);
        }
    }
/*
    public void replace(StringBuilder builder, String pattern, int value) {
        replace(builder, pattern, Integer.toString(value));
    }
*/

    public void updateFormCaption() {
        String infoWDB=getResourceString(R.string.learn_info_wdb).replace("#WDB",Integer.toString(controller.getWordsCount()));
        String infoWIL=getResourceString(R.string.learn_info_wil).replace("#WIL", Integer.toString(controller.getWordsInLessonCount()));
        String infoCL=getResourceString(R.string.learn_info_cl).replace("#CL", Integer.toString(controller.getCurrentLessonNumber())+"/"+Integer.toString(controller.getMaxLessonNumber()));
        String infoCP=getResourceString(R.string.learn_info_cp).replace("#CP", Integer.toString(controller.getCurrentWordPosition()));
        //StringBuilder info = getResourceStringBuilder(R.string.learn_info);

        btnLearnInfoWDB.setText(infoWDB);
        btnLearnInfoWIL.setText(infoWIL);
        btnLearnInfoCL.setText(infoCL);
        btnLearnInfoCP.setText(infoCP);

    }

    public void updateCurrentWord() {
        Word word = controller.getCurrentWord();
        String currentWord=getResourceString(R.string.learn_current_word).replace("#W", word.getValue());
        String currentTranslation=getResourceString(R.string.learn_current_translation).replace("#T", word.getTranslation());
        String currentExample=getResourceString(R.string.learn_current_example).replace("#E", word.getExample());
        btnLearnCurrentWord.setText(currentWord);
        btnLearnCurrentTranslation.setText(currentTranslation);
        btnLearnCurrentExample.setText(currentExample);
        //tvLearnCurrentWord.setText(word.getValue());
        //tvLearnCurrentTranslation.setText(word.getTranslationAndExample());
        //tvLearnCurrentExample.setText(word.getExample());
        updateFormCaption();
    }

    public void btnLearnLessonPrevOnClick(View v) {
        controller.moveToPrevLesson();
        updateCurrentWord();
    }

    public void btnLearnWordPrevOnClick(View v) {
        controller.moveToPrevWord();
        updateCurrentWord();
    }

    public void btnLearnWordNextOnClick(View v) {
        controller.moveToNextWord();
        updateCurrentWord();

    }

    public void btnLearnLessonNextOnClick(View v) {
        controller.moveToNextLesson();
        updateCurrentWord();
    }

    public void btnLearnStartTestOnClick(View v) throws LearnControllerException {
        List<Word> wordsForTest = controller.getWordsForCurrentLesson();
        Intent intent = new Intent(this, TestActivity.class);
        intent.putExtra(Word.class.getName(), (Serializable) wordsForTest);
        startActivityForResult(intent, FORM_ID_TEST);
    }

    public void btnLearnExitOnClick(View v) {
        System.exit(0);
    }

    public void processResultTestIsLearned() {
        DialogHelper.showInfoMessage(this, getResourceStringBuilder(R.string.learn_lesson_is_learned).toString());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String tag = "LearnActivity.onActivityResult";
        Log.d(tag, "processing. resultCode=" + resultCode);
        if (resultCode == RESULT_OK) {
            if (requestCode == FORM_ID_TEST) {
                Log.i(tag, "result from TEST form");
                if (data.hasExtra(AppConst.LEARN_ACTIVITY_RESULT) ) {
                    String result=data.getStringExtra(AppConst.LEARN_ACTIVITY_RESULT);
                    Log.d(tag, "result="+result);
                    processResultTestIsLearned();

                } else {
                    Log.w(tag, "Unknown result code");
                }
            } // SEARCH_FORM_ID


        } else { // if (resultCode == RESULT_OK)
            Log.w(tag, "result code<>OK");
        }
    }

    public void showInfoForm() {
        showActivity(InfoActivity.class, FORM_ID_INFO);
    }

    public void btnLearnInfoOnClick(View v) {
        showInfoForm();
    }

    public void showSettingsForm() {
        showActivity(SettingsActivity.class, FORM_ID_SETTINGS);
    }

    public void btnLearnSettingsOnClick(View v) {
        showSettingsForm();
    }
}