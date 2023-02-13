package com.vladimirbaranov.todolist.domain.repository

import com.vladimirbaranov.todolist.domain.entity.NewTodoData
import com.vladimirbaranov.todolist.domain.entity.TodoData
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    fun getTodoListFlow(): Flow<List<TodoData>>
    suspend fun createTodo(newTodoData: NewTodoData): TodoData
    suspend fun removeTodo(todoData: TodoData)
}