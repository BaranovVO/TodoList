package com.vladimirbaranov.todolist.di.modules

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.vladimirbaranov.todolist.TodoListApp
import com.vladimirbaranov.todolist.data.database.TodoDao
import com.vladimirbaranov.todolist.data.database.TodoDatabase
import com.vladimirbaranov.todolist.data.repository.TodoRepositoryImpl
import com.vladimirbaranov.todolist.domain.repository.TodoRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule() {
    companion object {
        private const val DATABASE_NAME = "TodoDB"
    }

    @Singleton
    @Provides
    fun provideTodoDatabase(context:Application): TodoDatabase {
        return Room.databaseBuilder(
            context,
            TodoDatabase::class.java, DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideTodoDao(database: TodoDatabase): TodoDao {
        return database.todoDao()
    }

    @Singleton
    @Provides
    fun provideTodoRepository(todoDao: TodoDao): TodoRepository {
        return TodoRepositoryImpl(todoDao)
    }
}