package com.vladimirbaranov.todolist.domain.usecases

import com.vladimirbaranov.todolist.domain.entity.TodoData
import com.vladimirbaranov.todolist.domain.repository.TodoRepository

class CreateTodoUseCase(
    private val todoRepository: TodoRepository
) {
    suspend fun execute(text: String): Result<TodoData> {
        return try {
            Result.success(todoRepository.createTodo(text))
        } catch (throwable: Throwable) {
            Result.failure(throwable)
        }
    }
}