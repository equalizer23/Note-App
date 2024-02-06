package com.todo.crud.feature_note.domain.use_case

import com.todo.crud.feature_note.data.repository.NoteRepositoryImpl
import com.todo.crud.feature_note.domain.model.InvalidNoteException
import com.todo.crud.feature_note.domain.model.Note
import com.todo.crud.feature_note.domain.repository.NoteRepository
import kotlin.jvm.Throws

class AddNoteUseCase(
    private val repo: NoteRepository
) {
    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note){
        if(note.title.isBlank()){
            throw InvalidNoteException("The title of the note cannot be empty.")
        }

        if(note.content.isBlank()){
            throw InvalidNoteException("The content of the note cannot be empty.")
        }
        repo.insertNote(note)
    }
}
