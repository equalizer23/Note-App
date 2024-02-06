package com.todo.crud.feature_note.data.repository

import com.todo.crud.feature_note.data.data_source.NoteDao
import com.todo.crud.feature_note.data.data_source.NoteDatabase
import com.todo.crud.feature_note.domain.model.Note
import com.todo.crud.feature_note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl(
    private val base: NoteDatabase
) : NoteRepository {
    override fun getNotes(): Flow<List<Note>> {
        return base.noteDao.getNotes()
    }

    override suspend fun getNoteById(id: Int): Note? {
        return base.noteDao.getNoteById(id)
    }

    override suspend fun insertNote(note: Note) {
        base.noteDao.insertNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        base.noteDao.deleteNote(note)
    }
}