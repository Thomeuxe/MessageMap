package com.thomaslecoeur.messagemap;

import android.content.Intent;
import android.net.Uri;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements LaunchMapFragment.OnFragmentInteractionListener, LaunchNoteListFragment.OnFragmentInteractionListener {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.openMapButtonWrapper, new LaunchMapFragment())
                .replace(R.id.openNoteListWrapper, new LaunchNoteListFragment())
                .commit();
    }

    @Override
    public void onFragmentInteraction() {
        Log.d(TAG, "click");
        startActivity(new Intent(MainActivity.this, MapActivity.class));
    }

    @Override
    public void onLaunchNoteListButtonFragmentInteraction() {
        Log.d(TAG, "onLaunchNoteListButtonFragmentInteraction: click");
        startActivity(new Intent(MainActivity.this, ListNoteActivity.class));
    }
}
