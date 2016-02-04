package com.thomaslecoeur.messagemap;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thomaslecoeur.messagemap.notes.Note;
import com.thomaslecoeur.messagemap.notes.NoteAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListNoteFragment extends Fragment {


    private static final String TAG = "ListNote";
    private NoteAdapter mNoteAdapter;

    public ListNoteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_note, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.notes_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));



//        Note note;
//        for (int i = 0; i < 3; i++) {
//            note = new Note("Pile à foin", "lala lala lalalala lala lalalala lala lalalala lala lalalala lala lala");
//            note.save();
//        }

        mNoteAdapter = new NoteAdapter(new ArrayList<Note>());

        mNoteAdapter.notifyDataSetChanged();

        mNoteAdapter.setNoteClickListener(new NoteAdapter.NoteClickListener() {

            @Override
            public void onClick(int position, View v) {
//                Note note = notes.get(position);
//                Log.d(TAG, "onClick / " + note);
//
//                Note noteCopy = new Note("Copy of " + note.getTitle(), "Description");
//                noteCopy.save();
//
//                mNoteAdapter.add(noteCopy);
//                mNoteAdapter.notifyItemInserted(mNoteAdapter.getItemCount() - 1);
            }
        });

        recyclerView.setAdapter(mNoteAdapter);

        loadData();

        return view;
    }

    private void loadData() {

        class NoteAsyncTask extends AsyncTask<Void, Void, Void> {
            List<Note> notes;

            @Override
            protected Void doInBackground(Void... params) {
                notes = Note.listAll(Note.class);
                mNoteAdapter.addAll(notes);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mNoteAdapter.notifyDataSetChanged();
            }
        }

        new NoteAsyncTask().execute();

    }

}
