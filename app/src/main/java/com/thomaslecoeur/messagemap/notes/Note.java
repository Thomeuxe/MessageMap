package com.thomaslecoeur.messagemap.notes;

import com.orm.SugarRecord;

/**
 * Created by thomaslecoeur on 04/02/16.
 */
public class Note extends SugarRecord {
    protected String mTitle;
    protected String mDescription;

    public Note() {

    }

    public Note(String title) {
        setTitle(title);
    }

    public Note(String title, String description) {
        setTitle(title);
        setDescription(description);
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

    @Override
    public String toString() {
        return "Note : " + mTitle + " - " + mDescription;
    }
}
