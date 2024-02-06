package com.todo.crud.feature_note.presentation.add_edit_note

import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todo.crud.feature_note.domain.model.InvalidNoteException
import com.todo.crud.feature_note.domain.model.Note
import com.todo.crud.feature_note.domain.use_case.NoteUseCases
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddEditNoteViewModel(
    private val cases: NoteUseCases
) : ViewModel(){
    private val _title = MutableStateFlow(NoteTextFieldState(
        hint = "Enter title"
    ))
    val title = _title.asStateFlow()

    private val _content = MutableStateFlow(NoteTextFieldState(
        hint = "Enter some content"
    ))
    val content = _content.asStateFlow()

    private val _color = MutableStateFlow(Note.noteColors.random().toArgb())
    val color = _color.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentNoteId: Int? = null

    fun getId(id: Int){
        if(id != -1){
            viewModelScope.launch {
                cases.getNote(id)?.also {note ->
                    currentNoteId = note.id
                    _title.tryEmit(title.value.copy(
                        text = note.title,
                        isHintVisible = false
                    ))

                    _content.tryEmit(content.value.copy(
                        text = note.content,
                        isHintVisible = false
                    ))

                    _color.tryEmit(note.color)
                }
            }
        }
    }

    fun onEvent(event: AddEditNoteEvent){
        when(event){
            is AddEditNoteEvent.EnteredTitle ->{
                _title.tryEmit(title.value.copy(
                    text = event.value
                ))
            }
            is AddEditNoteEvent.ChangeTitleFocus ->{
                _title.tryEmit(title.value.copy(
                    isHintVisible = !event.focusState.isFocused
                            && title.value.text.isBlank()
                ))
            }

            is AddEditNoteEvent.EnteredContent ->{
                _content.tryEmit(content.value.copy(
                    text = event.value
                ))
            }
            is AddEditNoteEvent.ChangeContentFocus ->{
                _content.tryEmit(content.value.copy(
                    isHintVisible = !event.focusState.isFocused
                            && content.value.text.isBlank()
                ))
            }

            is AddEditNoteEvent.ChangeColor ->{
                _color.tryEmit(event.color)
            }

            is AddEditNoteEvent.SaveNote ->{
                viewModelScope.launch {
                    try{
                        cases.addNote(
                            Note(
                                title = title.value.text,
                                content = content.value.text,
                                color = color.value,
                                timestamp = System.currentTimeMillis(),
                                id = currentNoteId
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveNote)
                    }catch(e: InvalidNoteException){
                        _eventFlow.emit(UiEvent.ShowSnackbar(
                            message = e.message ?: "Couldn't save note "
                        ))
                    }
                }
            }
        }
    }

    sealed class UiEvent{
        data class ShowSnackbar(val message: String) : UiEvent()
        object SaveNote : UiEvent()
    }
}