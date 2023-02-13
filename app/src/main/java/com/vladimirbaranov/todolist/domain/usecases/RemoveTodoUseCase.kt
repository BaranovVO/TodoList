package com.vladimirbaranov.todolist.domain.usecases

import com.vladimirbaranov.todolist.domain.entity.TodoData
import com.vladimirbaranov.todolist.domain.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoveTodoUseCase(
    private val todoRepository: TodoRepository
) {
    suspend fun execute(todoData: TodoData): Result<Unit> =
        withContext(Dispatchers.Default) {
            try {
                Result.success(todoRepository.removeTodo(todoData))
            } catch (throwable: Throwable) {
                Result.failure(throwable)
            }
        }
}