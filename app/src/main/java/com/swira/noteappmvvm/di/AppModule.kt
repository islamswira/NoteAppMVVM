package com.swira.noteappmvvm.di

import android.app.Application
import androidx.room.Room
import com.swira.noteappmvvm.feature_note.data.data_source.NoteDatabase
import com.swira.noteappmvvm.feature_note.data.repository.NoteRepositoryImpl
import com.swira.noteappmvvm.feature_note.domain.repository.NoteRepository
import com.swira.noteappmvvm.feature_note.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent :: class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application) : NoteDatabase{
        return Room.databaseBuilder(
            app,
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun ProvideNoteRepository(db: NoteDatabase): NoteRepository{
        return NoteRepositoryImpl(db.noteDao)
    }

    @Provides
    @Singleton
    fun ProvideNoteUseCases(repository: NoteRepository) : NoteUseCases{
        return NoteUseCases(
            getNotes = GetNotes(repository),
            deleteNote = DeleteNote(repository),
            addNote = AddNote(repository),
            getNote = GetNote(repository)
        )
    }
}