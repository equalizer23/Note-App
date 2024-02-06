package com.todo.crud.feature_note.presentation.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.todo.crud.feature_note.domain.model.Note
import com.todo.crud.feature_note.domain.use_case.NoteUseCases
import com.todo.crud.feature_note.domain.util.NoteOrder
import com.todo.crud.feature_note.domain.util.OrderType
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class NotesViewModel(
    private val cases: NoteUseCases
) : ViewModel() {

    private val _noteState = MutableStateFlow(NotesState())
    val noteState = _noteState.asStateFlow()

    private var recentlyDeletedNote: Note? = null
    private var getNotesJob: Job? = null

    init{
        getNotes(NoteOrder.Date(OrderType.Descending))
    }

    fun onEvent(event: NotesEvent){
        when(event){
            is NotesEvent.Order ->{
                if(noteState.value.noteOrder::class == event.noteOrder::class
                    && noteState.value.noteOrder.type == event.noteOrder.type){
                    return
                }
                getNotes(event.noteOrder)
            }
            is NotesEvent.DeleteNote ->{
                viewModelScope.launch {
                    cases.deleteNotes(event.note)
                    recentlyDeletedNote = event.note
                }
            }
            is NotesEvent.RestoreNote ->{
                viewModelScope.launch {
                    cases.addNote(recentlyDeletedNote ?: return@launch)
                    recentlyDeletedNote = null
                }
            }
            is NotesEvent.ToggleOrderSection ->{
                _noteState.tryEmit(noteState.value.copy(
                    isOrderSectionVisible = !noteState.value.isOrderSectionVisible
                ))
            }
        }
    }

    private fun getNotes(noteOrder: NoteOrder){
        getNotesJob?.cancel()
        getNotesJob = cases.getNotes(noteOrder).onEach {notes ->
            _noteState.tryEmit(
                noteState.value.copy(
                    notes = notes,
                    noteOrder = noteOrder
                )
            )
        }.launchIn(viewModelScope)
    }
}