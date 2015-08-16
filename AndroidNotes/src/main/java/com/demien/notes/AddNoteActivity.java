package com.demien.notes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import com.demien.R;

import java.util.Date;

/**
 * Created by dmitry on 04.11.14.
 */
public class AddNoteActivity extends Activity {

    EditText edtNoteDescription;
    //EditText edtNoteName;
    CheckBox cbIsPriority;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        //edtNoteName=(EditText)findViewById(R.id.edtAddNoteName);
        edtNoteDescription=(EditText)findViewById(R.id.edtAddNoteDescription);
        cbIsPriority=(CheckBox)findViewById(R.id.cbAddIsPriority);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_note, menu);
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


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void btnBackOnClick(View v) {
        finish();
    }

    public void processSaveOperation(){
        HelloAndroidActivity.NoteItem item=new HelloAndroidActivity.NoteItem();

        item.noteDate=new Date();
        //item.noteName=edtNoteName.getText().toString();
        item.noteDescription=edtNoteDescription.getText().toString();
        item.id= item.noteDate.getTime();
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

    public void btnAddOnClick(View v) {
        processSaveOperation();
    }
}