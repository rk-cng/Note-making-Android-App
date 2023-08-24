package com.singhinfo.enotes.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.singhinfo.enotes.Notes;
import com.singhinfo.enotes.NotesRepository;

import java.util.List;

public class NotesViewModel extends AndroidViewModel {

    public NotesRepository repository;
    public LiveData<List<Notes>> getAllNotes;
    public LiveData<List<Notes>> High2Low;
    public LiveData<List<Notes>> Low2High;

    public NotesViewModel(@NonNull Application application) {
        super(application);

        repository = new NotesRepository(application);
        getAllNotes = repository.getallNotes;
        High2Low = repository.High2Low;
        Low2High = repository.Low2High;

    }
    public void insertNote(Notes note) {
        repository.insertNote(note);
    }
    public void deleteNote(int id) {
        repository.deleteNote(id);
    }
    public void updateNote(Notes note) {
        repository.updateNote(note);
    }

}