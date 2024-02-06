package com.todo.crud.feature_note.presentation.notes

import com.todo.crud.feature_note.domain.model.Note
import com.todo.crud.feature_note.domain.util.NoteOrder
import com.todo.crud.feature_note.domain.util.OrderType

data class NotesState(
    val notes: List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)
