package com.singhinfo.enotes.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.singhinfo.enotes.Activity.UpdateNotes;
import com.singhinfo.enotes.MainActivity;
import com.singhinfo.enotes.Notes;
import com.singhinfo.enotes.R;
import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.notesViewHolder> {

    List<Notes> notes;
    List<Notes> searchedAllNotes;
    MainActivity mainActivity;

    public NotesAdapter(MainActivity mainActivity, List<Notes> notes) {
        this.notes = notes;
        this.mainActivity = mainActivity;
        searchedAllNotes = new ArrayList<>(notes);
    }

    public void searchedNotes(List<Notes> searchNote){
        this.notes = searchNote;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public notesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new notesViewHolder(LayoutInflater.from(mainActivity)
                .inflate(R.layout.notes_recyclerview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull notesViewHolder holder, int position) {

        Notes note = notes.get(position);

        switch (note.notePriority) {
            case "1":
                holder.notePriority.setBackgroundResource(R.drawable.red_shape);
                break;
            case "2":
                holder.notePriority.setBackgroundResource(R.drawable.yellow_shape);
                break;
            case "3":
                holder.notePriority.setBackgroundResource(R.drawable.green_shape);
                break;
        }

        holder.noteTitle.setText(note.noteTitle);
        holder.noteDate.setText(note.noteDate);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mainActivity, UpdateNotes.class);
            intent.putExtra("id",note.id);
            intent.putExtra("title",note.noteTitle);
            intent.putExtra("priority",note.notePriority);
            intent.putExtra("note",note.note);
            mainActivity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    static class notesViewHolder extends RecyclerView.ViewHolder {
        TextView noteTitle, noteDate;
        View notePriority;

        public notesViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.noteTitle);
            noteDate = itemView.findViewById(R.id.noteDate);
            notePriority = itemView.findViewById(R.id.notePriority);
        }
    }


}
