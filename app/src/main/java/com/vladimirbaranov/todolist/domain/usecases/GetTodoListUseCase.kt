package com.vladimirbaranov.todolist.domain.usecases

import com.vladimirbaranov.todolist.domain.entity.TodoData
import com.vladimirbaranov.todolist.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow

class GetTodoListUseCase(
    private val todoRepository: TodoRepository
) {
    suspend fun execute(): Result<Flow<List<TodoData>>> {
        return try {
            Result.success(todoRepository.getTodoList())
        } catch (throwable: Throwable) {
            Result.failure(throwable)
        }
    }
}