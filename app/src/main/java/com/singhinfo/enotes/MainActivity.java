package com.singhinfo.enotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.singhinfo.enotes.Activity.InsertNotes;
import com.singhinfo.enotes.Adapter.NotesAdapter;
import com.singhinfo.enotes.ViewModel.NotesViewModel;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton floatingBTN1;
    NotesViewModel notesViewModel;
    RecyclerView notesRecyclerView;
    NotesAdapter notesAdapter;
    TextView noFilter,h2lFilter,l2hFilter;
    List<Notes> searchedAllNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);

        floatingBTN1 = findViewById(R.id.floatingBTN1);
        noFilter = findViewById(R.id.noFilter);
        h2lFilter = findViewById(R.id.h2lFilter);
        l2hFilter = findViewById(R.id.l2hFilter);
        notesRecyclerView = findViewById(R.id.notesRecyclerView);
        noFilter.setBackgroundResource(R.drawable.pressed_background);

        notesViewModel.getAllNotes.observe(this, new Observer<List<Notes>>() {
        @Override
        public void onChanged(List<Notes> notes) {
            setNotesAdapter(notes);
            searchedAllNotes = notes;
        }
    });

        noFilter.setOnClickListener(v -> {
            loadNotes(0);
            noFilter.setBackgroundResource(R.drawable.pressed_background);
            l2hFilter.setBackgroundResource(R.drawable.unpressed_background);
            h2lFilter.setBackgroundResource(R.drawable.unpressed_background);
        });

        h2lFilter.setOnClickListener(v -> {
            loadNotes(1);
            h2lFilter.setBackgroundResource(R.drawable.pressed_background);
            l2hFilter.setBackgroundResource(R.drawable.unpressed_background);
            noFilter.setBackgroundResource(R.drawable.unpressed_background);

        });

        l2hFilter.setOnClickListener(v -> {
            loadNotes(2);
            l2hFilter.setBackgroundResource(R.drawable.pressed_background);
            noFilter.setBackgroundResource(R.drawable.unpressed_background);
            h2lFilter.setBackgroundResource(R.drawable.unpressed_background);

        });

        floatingBTN1.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, InsertNotes.class));
        });

    }

    private void loadNotes(int i) {

        if (i==0){
            notesViewModel.getAllNotes.observe(this, new Observer<List<Notes>>() {
                @Override
                public void onChanged(List<Notes> notes) {
                    setNotesAdapter(notes);
                    searchedAllNotes = notes;
                }
            });

        } else if (i==1){
            notesViewModel.High2Low.observe(this, new Observer<List<Notes>>() {
                @Override
                public void onChanged(List<Notes> notes) {
                    setNotesAdapter(notes);
                    searchedAllNotes = notes;
                }
            });

        } else {
            notesViewModel.Low2High.observe(this, new Observer<List<Notes>>() {
                @Override
                public void onChanged(List<Notes> notes) {
                    setNotesAdapter(notes);
                    searchedAllNotes = notes;
                }
            });

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search_menu,menu);

        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView)menuItem.getActionView();
        searchView.setQueryHint("search your note");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                noteSearch(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void setNotesAdapter(List<Notes> notes){
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        notesAdapter = new NotesAdapter(this, notes);
        notesRecyclerView.setAdapter(notesAdapter);
    }

    private void noteSearch(String newText) {

        ArrayList<Notes> note = new ArrayList<>();

        for (Notes notes : this.searchedAllNotes){
            if (notes.noteTitle.contains(newText)){
                note.add(notes);
            }
        }
        this.notesAdapter.searchedNotes(note);
    }

}