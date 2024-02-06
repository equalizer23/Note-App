package com.todo.crud.feature_note.presentation.util

sealed class Screen(val route: String){
    object NotesScreen : Screen("notes")
    object AddEditNotesScreen : Screen("add_edit")
}
