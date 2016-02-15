package com.thomaslecoeur.messagemap;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.thomaslecoeur.messagemap.notes.Note;

public class SingleNoteActivity extends AppCompatActivity implements SingleNoteActivityFragment.SingleNoteActivityFragmentListener {

    private static final String TAG = "SingleNoteActivity";
    protected Note mNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Long nId = getIntent().getExtras().getLong("CURRENT_NOTE_ID");
        mNote = Note.findById(Note.class, nId);

        setContentView(R.layout.activity_single_note);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Log.d(TAG, "onCreate: " + mNote);
        toolbar.setTitle(mNote.getTitle());
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_single_note, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public Note getNote() {
        return mNote;
    }
}
