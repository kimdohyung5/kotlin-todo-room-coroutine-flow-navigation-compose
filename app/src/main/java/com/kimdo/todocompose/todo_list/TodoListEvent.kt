package com.kimdo.todocompose.todo_list

import com.kimdo.todocompose.data.Todo


sealed class TodoListEvent {
    data class onDeleteTodoClick(val todo: Todo): TodoListEvent()
    data class onDoneChange(val todo: Todo, val isDone: Boolean): TodoListEvent()
    object OnUndoDeleteClick: TodoListEvent()
    data class OnTodoClick(val todo: Todo): TodoListEvent()
    object OnAddTodoClick: TodoListEvent()

}

