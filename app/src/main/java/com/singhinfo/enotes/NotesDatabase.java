package com.singhinfo.enotes;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Notes.class}, version = 1, exportSchema = false)
public abstract class NotesDatabase extends RoomDatabase {

    public abstract Dao dao();

    public static NotesDatabase INSTANCE;

    public static NotesDatabase getDatabaseInstance(final Context context) {
        if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NotesDatabase.class, "Notes_Database").allowMainThreadQueries()
                            .build();

        }
        return INSTANCE;
    }
}
