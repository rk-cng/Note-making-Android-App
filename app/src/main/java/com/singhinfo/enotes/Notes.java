package com.singhinfo.enotes;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Notes_Database")
public class Notes {

    @PrimaryKey(autoGenerate = true)
            public int id;

    @ColumnInfo(name = "note_title")
    public String noteTitle;

    @ColumnInfo(name = "note_date")
    public String noteDate;

    @ColumnInfo(name = "note")
    public String note;

    @ColumnInfo(name = "note_priority")
    public String notePriority;

//    @ColumnInfo(name = "image_text")
//    public String imageText="";


}
