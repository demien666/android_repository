package com.demien.words.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.demien.words.R;
import com.demien.words.AppConst;
import com.demien.words.controller.TestController;
import com.demien.words.domain.Word;
import com.demien.words.exception.TestControllerException;
import com.demien.words.util.DialogHelper;

import java.util.List;

/**
 * Created by dmitry on 22.02.15.
 */
public class TestActivity extends BaseActivity {

    private TextView tvTestFormCaption;
    private TextView tvTestWord;
    private ListView lvTestOption;

    private TestController controller;
    private ArrayAdapter<String> arrayAdapter;
    private List<String> arrayStorage;
    private Word testWord;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_loyout);
        controller = new TestController(this);

        tvTestFormCaption = (TextView) findViewById(R.id.tvTestFormCaption);
        tvTestWord = (TextView) findViewById(R.id.tvTestWord);
        lvTestOption = (ListView) findViewById(R.id.lvTestOption);

        lvTestOption.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String transationOption = arrayStorage.get(i);
                processResult(transationOption);
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            List<Word> words = (List<Word>) extras.getSerializable(Word.class.getName());
            if (words != null && words.size() > 0) {
                //tvTestWord.setText(words.get(0).getValue());
                controller.setWords(words);
            }
        }
        showNextWord();
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

    public void showNextWord() {
        try {
            testWord = controller.getNextWord();

            tvTestWord.setText(testWord.getValue());
            arrayStorage = controller.getTranslationList();
            arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, arrayStorage);
            lvTestOption.setAdapter(arrayAdapter);
            tvTestFormCaption.setText(getResourceStringBuilder(R.string.test_form_caption).toString()+controller.wordsToProcess());
        } catch (TestControllerException e) {
            if (e.getMessage().equals(TestControllerException.LESSON_IS_LEARNED)) {
                exit(AppConst.LEARN_ACTIVITY_RESULT_LEARNED);
            }
        }

    }

    public void processResult(String result) {
        String tag="processResult";
        Log.i(tag, "started:"+result);
        if (testWord.getTranslation().equals(result)) {
            controller.removeCurrentWord();
            //Log.i(tag, "right");
            //Toast.makeText(this, "Right", 1000);
            showNextWord();
        } else {
            //Log.i(tag, "wrong");
            DialogHelper.showErrorMessage(this, "Wrong!");
        }
        Log.i(tag, "finished");
    }

    public void exit(String exitCode) {
        finishWithResult(AppConst.LEARN_ACTIVITY_RESULT, exitCode);
    }
}
