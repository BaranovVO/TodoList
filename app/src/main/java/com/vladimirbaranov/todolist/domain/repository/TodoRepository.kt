package com.vladimirbaranov.todolist.domain.repository

import com.vladimirbaranov.todolist.domain.entity.TodoData
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    fun getTodoList(): Flow<List<TodoData>>
    suspend fun createTodo(text: String): TodoData
    suspend fun removeTodo(todoData: TodoData)
}