package com.thomaslecoeur.messagemap;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thomaslecoeur.messagemap.notes.Note;

/**
 * A placeholder fragment containing a simple view.
 */
public class SingleNoteActivityFragment extends Fragment {

    private static final String TAG = "SingleNoteFragment";
    private SingleNoteActivityFragmentListener mListener;
    protected Note mNote;

    public SingleNoteActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_single_note, container, false);

        mNote = mListener.getNote();
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
