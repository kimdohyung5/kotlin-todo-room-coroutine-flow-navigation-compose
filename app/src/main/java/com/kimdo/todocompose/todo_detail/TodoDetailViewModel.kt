package com.kimdo.todocompose.todo_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kimdo.todocompose.data.Todo
import com.kimdo.todocompose.data.TodoRepository
import com.kimdo.todocompose.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoDetailViewModel @Inject constructor(private val repository: TodoRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var todo by mutableStateOf<Todo?>(null)
        private set

    var title by mutableStateOf("")
        private set

    var description by mutableStateOf("")
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        val todoId = savedStateHandle.get<Int>("todoId")!!
        if(todoId != -1) {
            viewModelScope.launch {
                repository.getTodoById(todoId)?.let { todo ->
                    title = todo.title
                    description = todo.description ?: ""
                    this@TodoDetailViewModel.todo = todo
                }
            }
        }
    }

    fun onEvent(event: TodoDetailEvent) {
        when(event) {
            is TodoDetailEvent.OnTitleChange -> {
                title = event.title
            }
            is TodoDetailEvent.OnDescriptionChange -> {
                description = event.description
            }
            is TodoDetailEvent.OnSaveTodoClick -> {
                viewModelScope.launch {
                    if(title.isBlank()) {
                        sendUiEvent(UiEvent.ShowSnackbar( message = "The title can't be empty"))
                        return@launch
                    }
                    repository.insertTodo(
                        Todo( title=title, description = description,
                        isDone = todo?.isDone ?: false, id = todo?.id)
                    )
                    sendUiEvent(UiEvent.PopBackStack)
                }
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}