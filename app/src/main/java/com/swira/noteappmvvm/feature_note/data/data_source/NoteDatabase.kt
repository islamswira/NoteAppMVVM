package com.swira.noteappmvvm.feature_note.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.swira.noteappmvvm.feature_note.domain.model.Note


@Database(
    entities = [Note::class],
    version = 1
)
abstract class NoteDatabase: RoomDatabase() {

    abstract val noteDao:NoteDao

    companion object{
        const val DATABASE_NAME = "nots_db"
    }
}