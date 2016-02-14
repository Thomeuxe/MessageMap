package com.thomaslecoeur.messagemap;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.thomaslecoeur.messagemap.navigation.CustomVerticalPager;
import com.thomaslecoeur.messagemap.notes.NoteAdapter;

import pro.alexzaitsev.freepager.library.view.core.VerticalPager;

public class MapActivity extends AppCompatActivity implements ListNoteFragment.ListNoteFragmentInteractionListener {
    
    private static final String TAG = "MapActivity";
    private NoteAdapter mNoteAdapter;

    private MapFragment mMapFragment;
    private CustomVerticalPager mVerticalPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mMapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mVerticalPager = (CustomVerticalPager) findViewById(R.id.verticalPager);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
    }

    @Override
    public void onClickOpenNote(LatLng pos) {
        mMapFragment.centerOnPosition(pos);
        mVerticalPager.scrollDown();
        Log.d(TAG, "onClickOpenNote: greeeat - " + pos);
    }
}
