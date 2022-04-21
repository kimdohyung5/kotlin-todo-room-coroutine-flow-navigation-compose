package com.kimdo.todocompose.todo_detail

sealed class TodoDetailEvent {
    data class OnTitleChange(val title: String): TodoDetailEvent()
    data class OnDescriptionChange( val description: String): TodoDetailEvent()
    object OnSaveTodoClick: TodoDetailEvent()
}