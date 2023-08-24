package com.singhinfo.enotes;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NotesRepository {

    public Dao dao;
    public LiveData<List<Notes>> getallNotes;
    public LiveData<List<Notes>> High2Low;
    public LiveData<List<Notes>> Low2High;

    public NotesRepository(Application application) {
        NotesDatabase database = NotesDatabase.getDatabaseInstance(application);
        dao = database.dao();
        getallNotes = dao.getallNotes();
        High2Low = dao.High2Low();
        Low2High = dao.Low2High();
    }

    public void insertNote(Notes note){
        dao.insert(note);
    }
    public void deleteNote(int id){
        dao.delete(id);
    }
    public void updateNote(Notes note){
        dao.update(note);
    }
}
