package com.swira.noteappmvvm.feature_note.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.swira.noteappmvvm.feature_note.domain.model.Note
import kotlinx.coroutines.flow.Flow

/**
 * "DAO"  it mean data base access Operation and we use this because we are working with Local Database which in Our case now it is ROOM
 */


@Dao
interface NoteDao {

    @Query("SELECT * FROM note")
    fun getNotes(): Flow<List<Note>>

    @Query("SELECT * FROM note WHERE id = :id")
    suspend fun getNoteByID(id:Int): Note?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)
}