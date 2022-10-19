package com.swira.noteappmvvm.feature_note.presentation.add_edit_note

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swira.noteappmvvm.feature_note.domain.model.InvalidNoteException
import com.swira.noteappmvvm.feature_note.domain.model.Note
import com.swira.noteappmvvm.feature_note.domain.use_case.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel(){

    private val _noteTitle = mutableStateOf(NoteTextFeildState(
        hint = "Enter title..."
    ))
    val noteTitle: State<NoteTextFeildState> = _noteTitle

    private val _noteContent = mutableStateOf(NoteTextFeildState(
        hint = "Enter some content..."
    ))
    val noteContent: State<NoteTextFeildState> = _noteContent

    private val _noteColor = mutableStateOf(Note.noteColor.random().toArgb())
    val noteColor: State<Int> = _noteColor

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    private var currentNoteId: Int? = null
    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1 ){
                viewModelScope.launch {
                    noteUseCases.getNote(noteId)?.also { note ->
                        currentNoteId = note.id
                        _noteTitle.value = noteTitle.value.copy(
                            text = note.title,
                            isHintVisible = false
                        )
                        _noteContent.value = noteContent.value.copy(
                            text = note.content,
                            isHintVisible = false
                        )
                        _noteColor.value = note.color
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditEvent){
        when(event){
            is AddEditEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(
                    text = event.value
                )
            }
            is AddEditEvent.ChangeTitleFocus -> {
                _noteTitle.value = noteTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            noteTitle.value.text.isBlank()
                )
            }
            is AddEditEvent.EnteredContent -> {
                _noteContent.value = noteContent.value.copy(
                    text = event.value
                )
            }
            is AddEditEvent.ChangeContentFocus -> {
                _noteContent.value = noteContent.value.copy(
                    isHintVisible = !event.focusState.isFocused &&
                            noteContent.value.text.isBlank()
                )
            }
            is AddEditEvent.ChangeColor -> {
                _noteColor.value = event.color
            }
            is AddEditEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        noteUseCases.addNote(
                            Note(
                                title = noteTitle.value.text,
                                content = noteContent.value.text,
                                timestamp = System.currentTimeMillis(),
                                color = noteColor.value,
                                id = currentNoteId
                            )
                        )
                        _eventFlow.emit(UIEvent.SaveNote)
                    }catch (e: InvalidNoteException){
                        _eventFlow.emit(
                            UIEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save Note"
                            )
                        )
                    }
                }
            }
        }

    }


    sealed class UIEvent{
        data class ShowSnackbar(val message: String): UIEvent()
        object SaveNote: UIEvent()
    }
}