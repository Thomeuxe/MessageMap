package com.thomaslecoeur.messagemap.notes;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thomaslecoeur.messagemap.R;

import java.util.List;

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

        public NoteViewHolder(View itemView) {
            super(itemView);

            mTitleView = (TextView) itemView.findViewById(R.id.noteTitle);
            mDescriptionView = (TextView) itemView.findViewById(R.id.noteDescription);

            itemView.setOnClickListener(this);
        }

        public TextView getTitleView() {
            return mTitleView;
        }

        public TextView getDescriptionView() {
            return mDescriptionView;
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
