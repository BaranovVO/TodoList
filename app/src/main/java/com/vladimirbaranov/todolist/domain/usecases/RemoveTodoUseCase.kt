package com.vladimirbaranov.todolist.domain.usecases

import com.vladimirbaranov.todolist.domain.entity.TodoData
import com.vladimirbaranov.todolist.domain.repository.TodoRepository

class RemoveTodoUseCase(
    private val todoRepository: TodoRepository
) {
    suspend fun execute(todoData: TodoData): Result<Unit> {
        return try {
            Result.success(todoRepository.removeTodo(todoData))
        } catch (throwable: Throwable) {
            Result.failure(throwable)
        }
    }
}