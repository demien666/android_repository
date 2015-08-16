package com.demien.notes;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.demien.R;
import com.demien.notes.util.OpenFileDialog;
import com.demien.notes.util.SettingsHelper;

/**
 * Created by dmitry on 28.10.14.
 */
public class SettingsActivity extends Activity {

    private EditText edtFileName;
    private EditText edtDirectory;
    private CheckBox cbDeleteConfirm;
    //private ImageButton btnSettingsBack;

    private final SettingsHelper settingsHelper=new SettingsHelper(this, AppConst.APP_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        edtFileName=(EditText)findViewById(R.id.edtSettingsFileName);
        edtDirectory=(EditText)findViewById(R.id.edtSettingsDirectory);
        cbDeleteConfirm=(CheckBox)findViewById(R.id.cbSettingsDeleteConfirm);
        //btnSettingsBack=(ImageButton)findViewById(R.id.btnSettingsBack);

        edtDirectory.setText(settingsHelper.getValue(AppConst.SETTINGS_DIRECTORY));
        String fileName=settingsHelper.getValue(AppConst.SETTINGS_FILE);
        if ((fileName!=null) && (fileName.length()>0)) {
            edtFileName.setText(fileName);
        } else {
            //btnSettingsBack.setEnabled(false);
        }
        String deleteConfirmFlag=settingsHelper.getValue(AppConst.SETTINGS_DELETE_CONFIRM);
        if (deleteConfirmFlag!=null && deleteConfirmFlag.equals("false") ) {
            cbDeleteConfirm.setChecked(false);
        } else {
            cbDeleteConfirm.setChecked(true);
        }


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

    public void showErrorMessage(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
    }

    public void checkNotNullAndFinish() {
        String dir=edtDirectory.getText().toString();
        String fname=edtFileName.getText().toString();
        if (dir==null || dir.length()==0 || fname==null || fname.length()==0) {
             showErrorMessage(getResources().getString(R.string.msgSettingsCheck));
        } else {
            finish();
        }


    }

    public void btnBackOnClick(View v) {
        checkNotNullAndFinish();
    }

    public void btnSettingsSelectDirectoryOnClick(View v) {
        OpenFileDialog openFileDialog=new OpenFileDialog(this);
        openFileDialog.setOpenDialogListener(new OpenDialogListener());
        openFileDialog.show();
    }

    public void processSaveOperation() {
        settingsHelper.putValue(AppConst.SETTINGS_DIRECTORY, edtDirectory.getText().toString());
        settingsHelper.putValue(AppConst.SETTINGS_FILE, edtFileName.getText().toString());
        settingsHelper.putValue(AppConst.SETTINGS_DELETE_CONFIRM, Boolean.toString( cbDeleteConfirm.isChecked() ));
        setResult(RESULT_OK);
        checkNotNullAndFinish();
    }

    public void btnSettingsSaveOnClick(View v) {
       processSaveOperation();
    }

    private class OpenDialogListener implements OpenFileDialog.OpenDialogListener {

        @Override
        public void OnSelectedFile(String fileName) {
            SettingsActivity.this.edtDirectory.setText(fileName);

        }
    }
}
