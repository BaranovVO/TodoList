package com.vladimirbaranov.todolist.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vladimirbaranov.todolist.ui.screens.addTodo.AddTodoViewModel
import com.vladimirbaranov.todolist.ui.screens.todoList.TodoListViewModel
import com.vladimirbaranov.todolist.ui.utils.ViewModelFactory
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(TodoListViewModel::class)
    abstract fun todoListViewModel(viewModel: TodoListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AddTodoViewModel::class)
    abstract fun addTodoViewModel(viewModel: AddTodoViewModel): ViewModel
}

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)