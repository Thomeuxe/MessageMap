package com.thomaslecoeur.messagemap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.views.MapView;
import com.thomaslecoeur.messagemap.notes.Note;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MapFragment extends Fragment implements MapView.OnMapLongClickListener, GoogleApiClient.ConnectionCallbacks, com.google.android.gms.location.LocationListener {

    private static final String TAG = "MAP_FRAGMENT";

    private static final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 10;

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    MapView mapView;

    GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;
    private Marker mCurrentLocationMarker;
    private FloatingActionButton mCenterCurrentPositionButton;


    private Icon mUserIcon;

    ArrayList<LatLng> markerList = new ArrayList<LatLng>();

    // Dialog
    static final int REQUEST_TAKE_PHOTO = 1;
    ImageView thumbnailView;
    String mCurrentPicturePath;
    File mCurrentPictureFile;


    public MapFragment() {
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
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        /*
         Center on current location button listener
          */

        mCenterCurrentPositionButton = (FloatingActionButton) view.findViewById(R.id.centerCurrentPositionButton);

        mCenterCurrentPositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                centerOnCurrentLocation();
            }
        });

        /*
         MapBox initialisation
          */

        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.setAccessToken(BuildConfig.MAPBOX_ACCESS_TOKEN);
        mapView.setStyleUrl(Style.MAPBOX_STREETS);
        mapView.setLatLng(new LatLng(40.73581, -73.99155));
        mapView.setZoom(11);
        mapView.setStyleUrl(Style.LIGHT);

        mapView.setOnMapLongClickListener(this);

        /*
         Restore marker from saved markers
          */

        List<Note> notes = Note.listAll(Note.class);
        Log.d(TAG, "onCreateView: createMarkers");
        for (int i = 0; i < notes.size(); i++) {
            Note note = notes.get(i);
            mapView.addMarker(new MarkerOptions()
                            .position(new LatLng(note.getLatitude(), note.getLongitude()))
                            .title(note.getTitle())
                            .snippet(note.getDescription())
            );
        }

//        if (savedInstanceState != null) {
//            if (savedInstanceState.containsKey("points")) {
//                markerList = savedInstanceState.getParcelableArrayList("points");
//                List<Note> notes = Note.listAll(Note.class);
//                Log.d(TAG, "onCreateView: createMarkers");
//                for (int i = 0; i < notes.size(); i++) {
//                    createMarker(notes.get(i).getLatLng());
//                }
//            }
//        }

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

        Log.d(TAG, "createMarker");

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
    public void onMapLongClick(final LatLng point) {

        Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(50);

        MaterialDialog dialog = new MaterialDialog.Builder(getContext())
                .title("Add note")
                .customView(R.layout.add_note_form, true)
                .positiveText("Save")
                .neutralText("Cancel")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        View view = dialog.getCustomView();
                        EditText title = (EditText) view.findViewById(R.id.addNoteTitle);
                        EditText description = (EditText) view.findViewById(R.id.addNoteDescription);

                        addNote(title.getText().toString(), description.getText().toString(), point);
                    }
                })
                .show();

        Button addImgButton = (Button) dialog.getCustomView().findViewById(R.id.addImgButton);
        thumbnailView = (ImageView) dialog.getCustomView().findViewById(R.id.thumbnail);

        addImgButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }

        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult - RequestCode: " + requestCode + " - ResultCode: " + resultCode + " - Data: " + data);

        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPicturePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getContext().sendBroadcast(mediaScanIntent);

        setPic();

        /*if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            Log.d(TAG, "onActivityResult: " + imageBitmap);
            //thumbnailView.setImageBitmap(imageBitmap);
        }*/
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPicturePath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = 200;
        int targetH = 200;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPicturePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPicturePath, bmOptions);
        thumbnailView.setImageBitmap(bitmap);
    }

    public void addNote(String title, String description, LatLng point) {
        Note note = new Note(title, description, point, mCurrentPicturePath);
        note.save();

        mapView.addMarker(new MarkerOptions()
                .position(point)
                .title(title)
                .snippet(description));

        markerList.add(point);

        Snackbar snackbar = Snackbar
                .make(mapView, "Note saved !", Snackbar.LENGTH_LONG);

        snackbar.show();
    }

    /**
     * GPS IMPLEMENTATION
     */

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected");

        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(mCurrentLocation == null)
            return;

        LocationRequest locationRequest;
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(100);
        locationRequest.setSmallestDisplacement(3);

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);

        LatLng currentLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

        mapView.setLatLng(currentLatLng);

        IconFactory iconFactory = IconFactory.getInstance(getContext());
        mUserIcon = iconFactory.fromResource(R.drawable.ic_directions_walk_24dp);

        onLocationChanged(mCurrentLocation);

        Log.d(TAG, "onConnected: " + mCurrentLocation.toString());
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
                .icon(mUserIcon));

        Log.d(TAG, "onLocationChanged: " + mCurrentLocation.toString());
    }

    /**
     * UI listeners
     */

    public void centerOnCurrentLocation() {
        Log.d(TAG, "CenterOnCurrentLocation");
        mapView.setLatLng(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), true);
    }
}
