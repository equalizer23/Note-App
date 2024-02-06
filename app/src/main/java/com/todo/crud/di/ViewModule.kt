package com.todo.crud.di

import com.todo.crud.feature_note.presentation.add_edit_note.AddEditNoteViewModel
import com.todo.crud.feature_note.presentation.notes.NotesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModule = module {
    viewModel {
        NotesViewModel(get())
    }

    viewModel{
        AddEditNoteViewModel(get())
    }
}