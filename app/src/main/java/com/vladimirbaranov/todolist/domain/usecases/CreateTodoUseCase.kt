package com.vladimirbaranov.todolist.domain.usecases

import com.vladimirbaranov.todolist.domain.entity.NewTodoData
import com.vladimirbaranov.todolist.domain.entity.TodoData
import com.vladimirbaranov.todolist.domain.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CreateTodoUseCase(
    private val todoRepository: TodoRepository
) {
    suspend fun execute(text: String): Result<TodoData> =
        withContext(Dispatchers.Default) {
            try {
                Result.success(todoRepository.createTodo(NewTodoData(text = text)))
            } catch (throwable: Throwable) {
                Result.failure(throwable)
            }
        }
}