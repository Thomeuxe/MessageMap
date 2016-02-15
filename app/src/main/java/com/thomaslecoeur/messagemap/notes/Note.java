package com.thomaslecoeur.messagemap.notes;

import android.location.Location;
import android.util.Log;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.orm.SugarRecord;

import java.util.Date;

/**
 * Created by thomaslecoeur on 04/02/16.
 */
public class Note extends SugarRecord {
    private static final String TAG = "Note entity";
    protected String mTitle;
    protected String mDescription;
    protected Double mLatitude;
    protected Double mLongitude;
    protected String mPicturePath;
    public Date mCreatedOn;

    public Note() {
        setCreatedOn(new Date());
    }

    public Note(String title, String description, LatLng latLng, String mPicturePath) {
        Log.d(TAG, "Note: " + mPicturePath);
        setTitle(title);
        setDescription(description);
        setPosition(latLng);
        setPicturePath(mPicturePath);
        setCreatedOn(new Date());
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

    public LatLng getLatLng() {
        return new LatLng(mLatitude, mLongitude);
    }

    public Date getCreatedOn() {
        return mCreatedOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.mCreatedOn = createdOn;
    }

    public String getPicturePath() {
        return mPicturePath;
    }

    public void setPicturePath(String picturePath) {
        this.mPicturePath = picturePath;
    }

    @Override
    public String toString() {
        return "Note : " + mCreatedOn + " : " + mTitle + " - " + mDescription;
    }
}
