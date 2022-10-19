package com.swira.noteappmvvm.feature_note.domain.use_case

import com.swira.noteappmvvm.feature_note.domain.model.InvalidNoteException
import com.swira.noteappmvvm.feature_note.domain.model.Note
import com.swira.noteappmvvm.feature_note.domain.repository.NoteRepository

class AddNote(
    private val repository: NoteRepository
) {
    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note){
        if (note.title.isBlank()){
            throw InvalidNoteException("The title of Note can't be Empty ..")
        }
        if (note.content.isBlank()){
            throw InvalidNoteException("The content of Note can't be Empty ..")
        }
        repository.insertNote(note)
    }
}