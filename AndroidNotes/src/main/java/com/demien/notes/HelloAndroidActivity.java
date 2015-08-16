package com.demien.notes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.demien.R;
import com.demien.notes.util.FileHelper;
import com.demien.notes.util.SettingsHelper;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.logging.FileHandler;

public class HelloAndroidActivity extends Activity {

    /**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized after 
     * previously being shut down then this Bundle contains the data it most 
     * recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
    // CONSTANTS

    private final int FORM_SETTINGS_ID = 1;
    private final int FORM_ADD_NOTE_ID = 2;
    private final int FORM_UPD_NOTE_ID = 3;
    private final SettingsHelper settingsHelper=new SettingsHelper(this, AppConst.APP_ID);

    private String prefDirectory;
    private String prefFileName;


    List<NoteItem> noteItemList=new ArrayList<NoteItem>();
    NoteListAdapter noteListAdapter = new NoteListAdapter();
    ListView lvNotes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvNotes = (ListView)findViewById(R.id.listViewNotes);

        lvNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                NoteItem item=getItemById(id);
                showUpdateNoteForm(item);
            }
        });

        loadPreferences();
        if ((prefDirectory==null)||(prefDirectory.length()<1)) {
            showSettingsForm();
        }
        loadNotesFromFile();
        refreshList();

    }

    public void loadPreferences() {
        prefDirectory=settingsHelper.getValue(AppConst.SETTINGS_DIRECTORY);
        prefFileName=settingsHelper.getValue(AppConst.SETTINGS_FILE);
    }

    public String getStorageFileName() {
        return prefDirectory+"/"+prefFileName;
    }

    public void saveNotesToFile() {
        FileHelper<NoteItem> fileHelper=new FileHelper<NoteItem>();
        try {
            fileHelper.saveObjectListToFile(this, getStorageFileName(), noteItemList, NoteItem.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void loadNotesFromFile() {
        FileHelper<NoteItem> fileHelper=new FileHelper<NoteItem>();

        try {
            noteItemList=fileHelper.loadListFromFile(this, getStorageFileName(),NoteItem.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void refreshList() {
        //noteItemList = getDataForListView();
        Collections.sort(noteItemList);
        lvNotes.setAdapter(noteListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(com.demien.R.menu.main, menu);
	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    public void btnExitOnClick(View v) {
        System.exit(0);
    }

    public void btnSettingsOnClick(View v) {
        showSettingsForm();
    }

    public void btnAddNoteOnClick(View v) {
        showAddNoteForm();
    }

    public void btnInfoOnClick(View v) {
        //loadNotesFromFile();
        showInfoForm();
    }


    public void showInfoForm() {
        Intent intent = new Intent(this, InfoActivity.class);
        startActivity(intent);

    }

    public void showSettingsForm() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, FORM_SETTINGS_ID);

    }
    public void showAddNoteForm() {
        Intent intent=new Intent(this, AddNoteActivity.class);
        startActivityForResult(intent, FORM_ADD_NOTE_ID);
    }

    public void showUpdateNoteForm(NoteItem item) {
        Intent intent=new Intent(this, EditNoteActivity.class);
        intent.putExtra(NoteItem.class.getName(), item);
        startActivityForResult(intent, FORM_UPD_NOTE_ID);
    }

    public NoteItem getItemById(Long itemId) {
        for (NoteItem eachItem:noteItemList) {
            if (eachItem.id.equals(itemId)) {
                return eachItem;
            }
        }
        return null;
    }

    public void  updateNote(NoteItem item) {
        NoteItem actualItem=getItemById(item.id);
        actualItem.noteName=item.noteName;
        actualItem.noteDescription=item.noteDescription;
        actualItem.noteDate=item.noteDate;
        actualItem.priority=item.priority;
        refreshList();

    }

    public void  deleteNote(NoteItem item) {
        noteItemList.remove(getItemById(item.id));
        refreshList();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String tag = "onActivityResult";
        //Log.i(tag, "processing. resultCode=" + resultCode);
        if (resultCode == RESULT_OK) {
            if (requestCode == FORM_SETTINGS_ID) {
                //Log.i("onActivityResult", "processing serttings form result");
                loadPreferences();
                loadNotesFromFile();
                refreshList();

            }
            if (requestCode == FORM_ADD_NOTE_ID) {
                Log.i("onActivityResult", "processing add note form result");
                NoteItem item=(NoteItem)data.getSerializableExtra(NoteItem.class.getName());
                if ((item!=null) && (item.noteName!=null || item.noteDescription!=null)) {
                    noteItemList.add(item);
                    saveNotesToFile();
                    refreshList();
                }


            }

            if (requestCode == FORM_UPD_NOTE_ID) {
                Log.i("onActivityResult", "processing update note form result");
                NoteItem item=(NoteItem)data.getSerializableExtra(NoteItem.class.getName());
                if ((item!=null) && (item.noteName!=null || item.noteDescription!=null )) {
                    if (item.noteDescription.equals(AppConst.FLAG_DELETED)) {
                        deleteNote(item);
                    } else {
                        updateNote(item);
                    }
                    saveNotesToFile();
                    refreshList();
                }


            }

        }
    }

    public static class NoteItem implements Serializable, Comparable<NoteItem>{
        String noteName;
        String noteDescription;
        Date noteDate;
        Integer priority;
        Long id;

        public NoteItem() {
            noteDate=new Date();
        }

        public NoteItem(String noteName, String noteDescription) {
            this();
            this.noteName=noteName;
            this.noteDescription=noteDescription;
        }

        public NoteItem(String noteName, String noteDescription, Integer priority) {
            this(noteName, noteDescription);
            this.priority=priority;
        }

        @Override
        public int compareTo(NoteItem another) {

            String keyAnother=another.priority+Long.toString(another.id).substring(5);
            String keyThis=this.priority+Long.toString(this.id).substring(5);

            int valueAnother=Integer.parseInt(keyAnother);
            int valueThis=Integer.parseInt(keyThis);


            return valueAnother-valueThis;
        }
    }


    public class NoteListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return noteItemList.size();
        }

        @Override
        public NoteItem getItem(int arg0) {
            return noteItemList.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            NoteItem item = noteItemList.get(arg0);
            return item.id;
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            if(arg1==null)
            {
                LayoutInflater inflater = (LayoutInflater) HelloAndroidActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                arg1 = inflater.inflate(R.layout.note_list_item_layout, arg2,false);
            }

            //TextView chapterName = (TextView)arg1.findViewById(R.id.textView1);
            TextView chapterDesc = (TextView)arg1.findViewById(R.id.textView1);

            ImageView imageView=(ImageView)arg1.findViewById(R.id.imageView1);

            NoteItem item = noteItemList.get(arg0);

            //chapterName.setText(item.noteName);
            chapterDesc.setText(item.noteDescription);
            Resources res = getResources();
            if ((item.priority!=null) && (item.priority==2)) {
                imageView.setImageDrawable(res.getDrawable(R.drawable.icon_note_important));
            } else {
                imageView.setImageDrawable(res.getDrawable(R.drawable.icon_note));
            }


            return arg1;
        }

    }

}

