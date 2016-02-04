package com.thomaslecoeur.messagemap;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.views.MapView;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MapActivityFragment extends Fragment implements MapView.OnMapLongClickListener, GoogleApiClient.ConnectionCallbacks, com.google.android.gms.location.LocationListener {

    private static final String TAG = "MAP_FRAGMENT";

    private static final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 10;

    MapView mapView;

    GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Location mCurrentLocation;
    private Marker mCurrentLocationMarker;

    ArrayList<LatLng> markerList = new ArrayList<LatLng>();

    public MapActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

        super.onCreate(savedInstanceState);
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

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("points")) {
                markerList = savedInstanceState.getParcelableArrayList("points");
                if (markerList != null) {
                    for (int i = 0; i < markerList.size(); i++) {
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
        markerOptions.title("Lat:" + point.getLatitude() + "," + "Lng:" + point.getLongitude());

        // Adding marker on map
        mapView.addMarker(markerOptions);
    }

    /*********
     * mapBox listener implementation
     */

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        mapView.onStart();
        super.onStart();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        mapView.onStop();
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        //mapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
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

    /**
     * GPS IMPLEMENTATION
     */

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected");

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        LocationRequest locationRequest;
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(100);

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);

        LatLng currentLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

        mapView.setLatLng(currentLatLng);

        mCurrentLocationMarker = mapView.addMarker(new MarkerOptions()
                .position(currentLatLng)
                .title("My Position"));

        Log.d(TAG, "onConnected: " + mLastLocation.toString());
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended: " + i);
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;

        LatLng currentLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

        mapView.removeMarker(mCurrentLocationMarker);

        mCurrentLocationMarker = mapView.addMarker(new MarkerOptions()
                .position(currentLatLng)
                .title("Hello World!")
                .snippet("Welcome to my marker."));

        float sTmpBearing = (float) mapView.getBearing();
        mapView.setLatLng(currentLatLng);
        mapView.setBearing(sTmpBearing);

        Log.d(TAG, "onLocationChanged: " + mCurrentLocation.toString());
    }
}
