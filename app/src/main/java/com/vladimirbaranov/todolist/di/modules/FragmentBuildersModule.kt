package com.vladimirbaranov.todolist.di.modules

import com.vladimirbaranov.todolist.ui.screens.addTodo.AddTodoFragment
import com.vladimirbaranov.todolist.ui.screens.todoList.TodoListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun provideTodoListFragment(): TodoListFragment

    @ContributesAndroidInjector
    abstract fun provideAddTodoFragment(): AddTodoFragment
}