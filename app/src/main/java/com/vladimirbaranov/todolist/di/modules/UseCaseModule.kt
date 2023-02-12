package com.vladimirbaranov.todolist.di.modules

import com.vladimirbaranov.todolist.domain.repository.TodoRepository
import com.vladimirbaranov.todolist.domain.usecases.CreateTodoUseCase
import com.vladimirbaranov.todolist.domain.usecases.GetTodoListUseCase
import com.vladimirbaranov.todolist.domain.usecases.RemoveUseCase
import dagger.Module
import dagger.Provides

@Module
class UseCaseModule {
    @Provides
    fun provideGetTodoListUseCase(todoRepository: TodoRepository):GetTodoListUseCase{
        return GetTodoListUseCase(todoRepository)
    }

    @Provides
    fun provideCreateTodoUseCase(todoRepository: TodoRepository):CreateTodoUseCase{
        return CreateTodoUseCase(todoRepository)
    }

    @Provides
    fun provideRemoveUseCase(todoRepository: TodoRepository):RemoveUseCase{
        return RemoveUseCase(todoRepository)
    }
}