package com.todo.crud.feature_note.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.todo.crud.feature_note.domain.model.Note
import com.todo.crud.feature_note.presentation.add_edit_note.components.AddEditNoteScreen
import com.todo.crud.feature_note.presentation.notes.components.NotesScreen
import com.todo.crud.feature_note.presentation.util.Screen
import com.todo.crud.ui.theme.NoteAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val controller = rememberNavController()
                    NavHost(navController = controller, startDestination = Screen.NotesScreen.route){
                        composable(Screen.NotesScreen.route){
                            NotesScreen(controller = controller)
                        }

                        composable("${Screen.AddEditNotesScreen.route}?id={id}&color={color}",
                            arguments = listOf(navArgument("id"){
                                type = NavType.IntType
                                defaultValue = -1
                            }, navArgument("color"){
                                type = NavType.IntType
                                defaultValue = -1
                            })
                        ){
                            val id = it.arguments?.getInt("id")
                            val color = it.arguments?.getInt("color")
                            AddEditNoteScreen(controller = controller,
                                color = color ?: Note.noteColors.random().toArgb(), id = id ?: -1)
                        }
                    }
                }
            }
        }
    }
}