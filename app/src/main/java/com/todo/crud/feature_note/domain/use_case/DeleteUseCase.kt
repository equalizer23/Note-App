package com.todo.crud.feature_note.domain.use_case

import com.todo.crud.feature_note.domain.model.Note
import com.todo.crud.feature_note.domain.repository.NoteRepository

class DeleteNoteUseCase(
    private val repo: NoteRepository
) {
    suspend operator fun invoke(note: Note){
        repo.deleteNote(note)
    }
}