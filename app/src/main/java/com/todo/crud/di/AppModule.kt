package com.todo.crud.di

import android.app.Application
import androidx.room.Room
import com.todo.crud.NoteApp
import com.todo.crud.feature_note.data.data_source.NoteDatabase
import com.todo.crud.feature_note.data.repository.NoteRepositoryImpl
import com.todo.crud.feature_note.domain.repository.NoteRepository
import com.todo.crud.feature_note.domain.use_case.AddNoteUseCase
import com.todo.crud.feature_note.domain.use_case.DeleteNoteUseCase
import com.todo.crud.feature_note.domain.use_case.GetNotesByIdUseCase
import com.todo.crud.feature_note.domain.use_case.GetNotesUseCase
import com.todo.crud.feature_note.domain.use_case.NoteUseCases
import org.koin.dsl.module

val appModule = module {
    factory<NoteRepository>{
        NoteRepositoryImpl(base = get())
    }

    single<Application>{
        NoteApp()
    }


    factory{
        NoteUseCases(
            getNotes = GetNotesUseCase(get()),
            deleteNotes = DeleteNoteUseCase(get()),
            addNote = AddNoteUseCase(get()),
            getNote = GetNotesByIdUseCase(get())
        )
    }

    single<NoteDatabase>{
        Room.databaseBuilder(
            get(),
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()
    }
}