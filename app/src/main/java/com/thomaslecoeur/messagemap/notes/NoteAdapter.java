package com.thomaslecoeur.messagemap.notes;

import android.location.Address;
import android.location.Geocoder;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thomaslecoeur.messagemap.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by thomaslecoeur on 04/02/16.
 */
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    private static final String TAG = "NoteAdapter";
    private List<Note> mNotes;
    private static NoteClickListener mListener;

    public NoteAdapter(List<Note> notes) {
        mNotes = notes;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        NoteViewHolder holder;

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_note, parent, false);

        holder = new NoteViewHolder(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        Note note = mNotes.get(position);

        holder.getTitleView().setText(note.getTitle());
        holder.getDescriptionView().setText(note.getDescription());

        Geocoder geocoder = new Geocoder(holder.getGeocoderView().getContext(), Locale.getDefault());

        List<Address> addresses = new ArrayList();

        try {
            addresses = geocoder.getFromLocation(note.getLatitude(), note.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "onBindViewHolder: " + addresses);

        // Handle case where no address was found.
        if (addresses != null || addresses.size()  != 0) {
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();

            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread.
            for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }

            holder.getGeocoderView().setText(TextUtils.join(System.getProperty("line.separator"),
                    addressFragments));
        }

    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public void setNoteClickListener(NoteClickListener listener) {
        mListener = listener;
    }

    public void add(Note note) {
        mNotes.add(note);
    }

    public void addAll(List<Note> notes) {
        mNotes.addAll(notes);
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mTitleView;
        private final TextView mDescriptionView;
        private final TextView mGeocoderView;

        public NoteViewHolder(View itemView) {
            super(itemView);

            mTitleView = (TextView) itemView.findViewById(R.id.noteTitle);
            mDescriptionView = (TextView) itemView.findViewById(R.id.noteDescription);
            mGeocoderView = (TextView) itemView.findViewById(R.id.noteGeocoder);

            itemView.setOnClickListener(this);
        }

        public TextView getTitleView() {
            return mTitleView;
        }

        public TextView getDescriptionView() {
            return mDescriptionView;
        }

        public TextView getGeocoderView() {
            return mGeocoderView;
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick: " + v);

            if(mListener == null) {
                return;
            }

            mListener.onClick(getAdapterPosition(), v);
        }
    }

    public interface NoteClickListener {
        void onClick(int position, View v);
    }

}
