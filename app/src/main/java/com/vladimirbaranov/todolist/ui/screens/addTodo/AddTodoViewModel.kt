package com.vladimirbaranov.todolist.ui.screens.addTodo

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladimirbaranov.todolist.domain.entity.TodoData
import com.vladimirbaranov.todolist.domain.usecases.CreateTodoUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddTodoViewModel @Inject constructor(
    private val createTodoUseCase: CreateTodoUseCase
) : ViewModel(), DefaultLifecycleObserver {

    sealed class Event {
        data class Error(val throwable: Throwable) : Event()
        data class TodoAdded(val todoData: TodoData) : Event()
    }

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val events = eventChannel.receiveAsFlow()

    fun addTodo(text: String) {
        viewModelScope.launch {
            createTodoUseCase.execute(text)
                .onFailure { eventChannel.send(Event.Error(it)) }
                .onSuccess { eventChannel.send(Event.TodoAdded(it)) }
        }
    }
}