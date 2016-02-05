package com.thomaslecoeur.messagemap.notes;

import android.location.Location;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.orm.SugarRecord;

/**
 * Created by thomaslecoeur on 04/02/16.
 */
public class Note extends SugarRecord {
    protected String mTitle;
    protected String mDescription;
    protected Double mLatitude;
    protected Double mLongitude;

    public Note() {

    }

    public Note(String title) {
        setTitle(title);
    }

    public Note(String title, String description) {
        setTitle(title);
        setDescription(description);
    }

    public Note(String title, String description, LatLng latLng) {
        setTitle(title);
        setDescription(description);
        setPosition(latLng);
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String Description) {
        this.mDescription = Description;
    }

    public void setPosition(LatLng latLng) {
        setLatitude(latLng.getLatitude());
        setLongitude(latLng.getLongitude());
    }

    public Double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(Double latitude) {
        mLatitude = latitude;
    }

    public Double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(Double longitude) {
        mLongitude = longitude;
    }

    @Override
    public String toString() {
        return "Note : " + mTitle + " - " + mDescription;
    }
}
