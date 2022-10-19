package com.swira.noteappmvvm.feature_note.presentation.add_edit_note

data class NoteTextFeildState(
    val text : String = "",
    val hint : String = "",
    val isHintVisible : Boolean = true
)
