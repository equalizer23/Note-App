package com.todo.crud.feature_note.domain.use_case

import com.todo.crud.feature_note.data.repository.FakeNoteRepository
import com.todo.crud.feature_note.domain.model.InvalidNoteException
import com.todo.crud.feature_note.domain.model.Note
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertFailsWith

class AddNoteUseCaseTest{
    private lateinit var addNoteUseCase: AddNoteUseCase
    private lateinit var fakeRepo: FakeNoteRepository

    @Before
    fun setup(){
        fakeRepo = FakeNoteRepository()
        addNoteUseCase = AddNoteUseCase(fakeRepo)
    }

    @Test
    fun `Throws an error if title is blank`(){
        val note = Note(
            title = "",
            content = "Something",
            timestamp = 5,
            color = 1
        )
        runBlocking {
            assertFailsWith(InvalidNoteException::class) {
                addNoteUseCase(note)
            }
        }
    }

    @Test
    fun `Throws an error if content is blank`(){
        val note = Note(
            title = "Something",
            content = "",
            timestamp = 5,
            color = 1
        )
        runBlocking {
            assertFailsWith(InvalidNoteException::class) {
                addNoteUseCase(note)
            }
        }
    }

    @Test
    fun `Throws an error if both fields are blank`(){
        val note = Note(
            title = "",
            content = "",
            timestamp = 5,
            color = 1
        )
        runBlocking {
            assertFailsWith(InvalidNoteException::class) {
                addNoteUseCase(note)
            }
        }
    }


}