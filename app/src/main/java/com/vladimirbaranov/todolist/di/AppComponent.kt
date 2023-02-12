package com.vladimirbaranov.todolist.di

import android.app.Application
import com.vladimirbaranov.todolist.TodoListApp
import com.vladimirbaranov.todolist.di.modules.DataModule
import com.vladimirbaranov.todolist.di.modules.MainActivityModule
import com.vladimirbaranov.todolist.di.modules.UseCaseModule
import com.vladimirbaranov.todolist.di.modules.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        DataModule::class,
        UseCaseModule::class,
        MainActivityModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent : AndroidInjector<TodoListApp> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    override fun inject(app: TodoListApp)
}