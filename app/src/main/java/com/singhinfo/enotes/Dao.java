package com.singhinfo.enotes;

import androidx.lifecycle.LiveData;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@androidx.room.Dao
public interface Dao {

    @Query("SELECT *FROM Notes_Database")
    LiveData<List<Notes>> getallNotes();

    @Query("SELECT *FROM Notes_Database ORDER BY note_priority ASC")
    LiveData<List<Notes>> High2Low();

    @Query("SELECT *FROM Notes_Database ORDER BY note_priority DESC")
    LiveData<List<Notes>> Low2High();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Notes note);

    @Query("DELETE FROM Notes_Database WHERE id = :id")
    void delete(int id);

    @Update
    void update(Notes note);

}
