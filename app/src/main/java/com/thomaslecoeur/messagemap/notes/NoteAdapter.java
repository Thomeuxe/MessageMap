package com.thomaslecoeur.messagemap.notes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.thomaslecoeur.messagemap.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

        Log.d(TAG, "onBindViewHolder picture path: " + note.getPicturePath());

        if(note.getPicturePath() != null) {
            Log.d(TAG, "onBindViewHolder: " + note.getPicturePath());
            try {
                File f=new File(Environment.getExternalStorageDirectory() + File.separator, note.getPicturePath());
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                holder.getImageView().setImageBitmap(b);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
        }

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
        Log.d(TAG, "onBindViewHolder: " + addresses + " - " + addresses.size());
        if (addresses.size() != 0) {
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

    public Note getItem(int pos) {
        return mNotes.get(pos);
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
        private final TextView mGeocoderView;
        private final Button mButtonOpenInMap;
        private final Button mButtonOpenNote;
        private final ImageView mImageView;

        public NoteViewHolder(View itemView) {
            super(itemView);

            mTitleView = (TextView) itemView.findViewById(R.id.noteTitle);
            mGeocoderView = (TextView) itemView.findViewById(R.id.noteGeocoder);
            mButtonOpenInMap = (Button) itemView.findViewById(R.id.openNoteInMap);
            mButtonOpenNote = (Button) itemView.findViewById(R.id.openNote);
            mImageView = (ImageView) itemView.findViewById(R.id.notePicture);

            mButtonOpenInMap.setOnClickListener(this);
            mButtonOpenNote.setOnClickListener(this);
        }

        public TextView getTitleView() {
            return mTitleView;
        }

        public TextView getGeocoderView() {
            return mGeocoderView;
        }

        public ImageView getImageView() { return mImageView; }

        @Override
        public void onClick(View v) {
            if(mListener == null) {
                return;
            }

            if(v.getId() == R.id.openNote) {
                mListener.onClickOpenNote(getAdapterPosition());
            } else if(v.getId() == R.id.openNoteInMap) {
                mListener.onClickOpenMap(getAdapterPosition(), v);
            }

        }
    }

    public interface NoteClickListener {
        void onClickOpenMap(int position, View v);
        void onClickOpenNote(int position);
    }

}
