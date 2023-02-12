package com.vladimirbaranov.todolist.di.modules

import com.vladimirbaranov.todolist.ui.screens.todolist.TodoListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun provideodoListFragment(): TodoListFragment
}