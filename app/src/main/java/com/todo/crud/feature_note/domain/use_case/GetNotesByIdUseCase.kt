package com.todo.crud.feature_note.domain.use_case

import com.todo.crud.feature_note.domain.model.Note
import com.todo.crud.feature_note.domain.repository.NoteRepository

class GetNotesByIdUseCase(
    private val repo: NoteRepository
) {
    suspend operator fun invoke(id: Int) : Note?{
        return repo.getNoteById(id)
    }
}