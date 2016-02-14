package com.thomaslecoeur.messagemap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.mapbox.mapboxsdk.geometry.LatLng;

public class ListNoteActivity extends AppCompatActivity implements ListNoteFragment.ListNoteFragmentInteractionListener {

    private static final String TAG = "ListNoteActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_note);
    }

    @Override
    public void onClickOpenNote(LatLng pos) {
        Log.d(TAG, "onClickOpenNote: Click on note");
    }
}
