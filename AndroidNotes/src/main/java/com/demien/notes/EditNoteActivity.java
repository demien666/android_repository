package com.demien.notes;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import com.demien.R;
import com.demien.notes.util.SettingsHelper;

import java.util.Date;

/**
 * Created by dmitry on 04.11.14.
 */
public class EditNoteActivity extends Activity {

    //EditText edtNoteName;
    EditText edtNoteDescription;
    HelloAndroidActivity.NoteItem item;
    CheckBox cbIsPriority;
    private final SettingsHelper settingsHelper=new SettingsHelper(this, AppConst.APP_ID);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        //edtNoteName=(EditText)findViewById(R.id.edtEditNoteName);
        edtNoteDescription=(EditText)findViewById(R.id.edtEditNoteDescription);
        cbIsPriority=(CheckBox)findViewById(R.id.cbAddIsPriority);

        Bundle extras = getIntent().getExtras();
        if (extras!=null) {
            item=(HelloAndroidActivity.NoteItem)extras.getSerializable(HelloAndroidActivity.NoteItem.class.getName());
            //edtNoteName.setText(item.noteName);
            edtNoteDescription.setText(item.noteDescription);
            if ((item.priority!=null)&&(item.priority==2)) {
                cbIsPriority.setChecked(true);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.update_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_back:
                finish();
                return true;
            case R.id.action_saveNote:
                processSaveOperation();
                return true;
            case R.id.action_deleteNote:
                processDeleteOperation();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void processSaveOperation() {
        item.noteDate=new Date();
        //item.noteName=edtNoteName.getText().toString();
        item.noteDescription=edtNoteDescription.getText().toString();
        if (cbIsPriority.isChecked()) {
            item.priority=2;
        } else {
            item.priority=1;
        }

        Intent data=new Intent();
        data.putExtra(HelloAndroidActivity.NoteItem.class.getName(), item);

        setResult(RESULT_OK, data);
        finish();

    }

    public void btnEditNoteEditOnClick(View v) {
       processSaveOperation();
    }

    public void doDelete() {
        item.noteDescription=AppConst.FLAG_DELETED;

        Intent data=new Intent();
        data.putExtra(HelloAndroidActivity.NoteItem.class.getName(), item);

        setResult(RESULT_OK, data);
        finish();
    }

    public void processDeleteOperation() {
        String deleteConfirmationFlag=null;
        try {
            deleteConfirmationFlag = settingsHelper.getValue(AppConst.SETTINGS_DELETE_CONFIRM);
        } catch (NullPointerException e) {

        }

        if (deleteConfirmationFlag!=null && deleteConfirmationFlag.equals("false")) {
            doDelete();
        }

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.const_confirmation_caption)
                .setMessage(R.string.const_confirmation_text)
                .setPositiveButton(R.string.const_confirmation_yes, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doDelete();
                    }

                })
                .setNegativeButton(R.string.const_confirmation_no, null)
                .show();

    }

    public void btnEditNoteRemoveOnClick(View v) {
        processDeleteOperation();
    }

    public void btnBackOnClick(View v) {
        finish();
    }
}