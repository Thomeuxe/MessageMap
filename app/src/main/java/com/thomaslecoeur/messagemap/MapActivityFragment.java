package com.thomaslecoeur.messagemap;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.views.MapView;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MapActivityFragment extends Fragment implements MapView.OnMapLongClickListener {

    private static final String TAG = "MAP_FRAGMENT";
    MapView mapView;

    ArrayList<LatLng> markerList = new ArrayList<LatLng>();

    public MapActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.setAccessToken(BuildConfig.MAPBOX_ACCESS_TOKEN);
        mapView.setStyleUrl(Style.MAPBOX_STREETS);
        mapView.setLatLng(new LatLng(40.73581, -73.99155));
        mapView.setZoom(11);

        mapView.setOnMapLongClickListener(this);

        if(savedInstanceState!=null){
            if(savedInstanceState.containsKey("points")){
                markerList = savedInstanceState.getParcelableArrayList("points");
                if(markerList!=null){
                    for(int i=0;i<markerList.size();i++){
                        createMarker(markerList.get(i));
                    }
                }
            }
        }

        mapView.onCreate(savedInstanceState);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("points", markerList);
        mapView.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    /*********
     * Custom map method
     */

    public void createMarker(LatLng point) {

        MarkerOptions markerOptions = new MarkerOptions();

        // Setting latitude and longitude of the marker
        markerOptions.position(point);

        // Setting the title of the marker
        markerOptions.title("Lat:"+point.getLatitude()+","+"Lng:"+point.getLongitude());

        // Adding marker on map
        mapView.addMarker(markerOptions);
    }

    /*********
     * mapBox listener implementation
     */

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause()  {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onMapLongClick(LatLng point) {
        mapView.addMarker(new MarkerOptions()
                .position(point)
                .title("Hello World!")
                .snippet("Welcome to my marker."));

        markerList.add(point);
    }
}
