package com.thomaslecoeur.messagemap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.thomaslecoeur.messagemap.notes.Note;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A placeholder fragment containing a simple view.
 */
public class SingleNoteActivityFragment extends Fragment {

    private static final String TAG = "SingleNoteFragment";
    private SingleNoteActivityFragmentListener mListener;
    protected Note mNote;
    protected TextView mDateView;
    protected TextView mLocationView;
    protected ImageView mImageView;
    protected TextView mDescriptionView;

    public SingleNoteActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_single_note, container, false);

        mNote = mListener.getNote();

        mDateView = (TextView) v.findViewById(R.id.singleNoteDate);

        mDateView.setText(DateUtils.getRelativeTimeSpanString(
                mNote.getCreatedOn().getTime(),
                System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS
        ));

        mLocationView = (TextView) v.findViewById(R.id.singleNoteLocation);

        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

        List<Address> addresses = new ArrayList();

        try {
            addresses = geocoder.getFromLocation(mNote.getLatitude(), mNote.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        if (addresses.size() != 0) {
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();

            for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }

            mLocationView.setText(TextUtils.join(System.getProperty("line.separator"),
                    addressFragments));
        }

        mImageView = (ImageView) v.findViewById(R.id.singleNoteImage);

        if(mNote.getPicturePath() != null) {
            try {
                File f=new File(Environment.getExternalStorageDirectory() + File.separator, mNote.getPicturePath());
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                mImageView.setImageBitmap(b);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
        }


        mDescriptionView = (TextView) v.findViewById(R.id.singleNoteDescription);

        mDescriptionView.setText(mNote.getDescription());

        Log.d(TAG, "onCreateView: " + mNote.getDescription());

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SingleNoteActivityFragmentListener) {
            mListener = (SingleNoteActivityFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SingleNoteActivityFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface SingleNoteActivityFragmentListener {
        Note getNote();
    }
}
