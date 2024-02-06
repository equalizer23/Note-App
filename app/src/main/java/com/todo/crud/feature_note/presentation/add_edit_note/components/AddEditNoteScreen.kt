package com.todo.crud.feature_note.presentation.add_edit_note.components

import android.annotation.SuppressLint
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.todo.crud.feature_note.domain.model.Note
import com.todo.crud.feature_note.presentation.add_edit_note.AddEditNoteEvent
import com.todo.crud.feature_note.presentation.add_edit_note.AddEditNoteViewModel
import com.todo.crud.feature_note.presentation.notes.NotesEvent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    controller: NavHostController,
    color: Int,
    id: Int
) {
    val viewModel = getViewModel<AddEditNoteViewModel>()
    val title = viewModel.title.collectAsState()
    val content = viewModel.content.collectAsState()
    val colorState = viewModel.color.collectAsState()

    val noteBackgroundAnimatable = remember{
        Animatable(
            Color(if(color != -1) color else colorState.value)
        )
    }

    val snackBarState = remember {
        SnackbarHostState()
    }
    
    LaunchedEffect(key1 = null){
        viewModel.getId(id)
    }
    
    LaunchedEffect(key1 = true){
        viewModel.eventFlow.collectLatest {event ->
            when(event){
                is AddEditNoteViewModel.UiEvent.ShowSnackbar ->{
                    snackBarState
                        .showSnackbar(
                            message = event.message,
                        )
                }
                is AddEditNoteViewModel.UiEvent.SaveNote ->{
                    controller.navigateUp()
                }
            }
        }
    }

    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarState)},
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(AddEditNoteEvent.SaveNote)
            }, containerColor = MaterialTheme.colorScheme.primary, shape = CircleShape) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Save Note")
            }
        }
    ){
        Column(modifier = Modifier
            .fillMaxSize()
            .background(noteBackgroundAnimatable.value)
            .padding(16.dp)
        ){
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Note.noteColors.forEach { color ->
                    val colorInt = color.toArgb()
                    Box(modifier = Modifier
                        .size(50.dp)
                        .shadow(15.dp, CircleShape)
                        .clip(CircleShape)
                        .background(color)
                        .border(
                            width = 3.dp,
                            color = if (colorState.value == colorInt) {
                                Color.Black
                            } else {
                                Color.Transparent
                            },
                            shape = CircleShape
                        )
                        .clickable {
                            scope.launch {
                                noteBackgroundAnimatable.animateTo(
                                    targetValue = Color(colorInt),
                                    animationSpec = tween(
                                        durationMillis = 500
                                    )
                                )
                            }

                            viewModel.onEvent(AddEditNoteEvent.ChangeColor(colorInt))
                        }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            TransparentTextField(
                text = title.value.text,
                hint = title.value.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditNoteEvent.EnteredTitle(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditNoteEvent.ChangeTitleFocus(it))
                },
                isHintVisible = title.value.isHintVisible,
                singleLine = true,
                textStyle = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(16.dp))
            TransparentTextField(
                text = content.value.text,
                hint = content.value.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditNoteEvent.EnteredContent(it))
                },
                onFocusChange = {
                    viewModel.onEvent(AddEditNoteEvent.ChangeContentFocus(it))
                },
                isHintVisible = content.value.isHintVisible,
                singleLine = false,
                textStyle = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxHeight()
            )
        }
    }
}