package com.vladimirbaranov.todolist.ui.screens.todoList

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladimirbaranov.todolist.domain.entity.TodoData
import com.vladimirbaranov.todolist.domain.usecases.GetTodoListUseCase
import com.vladimirbaranov.todolist.domain.usecases.RemoveTodoUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class TodoListViewModel @Inject constructor(
    private val getTodoListUseCase: GetTodoListUseCase,
    private val removeTodoUseCase: RemoveTodoUseCase
) : ViewModel(), DefaultLifecycleObserver {

    sealed class Event {
        data class Error(val throwable: Throwable) : Event()
        data class TodoRemoved(val todoData: TodoData) : Event()
    }

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val events = eventChannel.receiveAsFlow()

    private val _todoList = MutableStateFlow(listOf<TodoData>())
    val todoList: StateFlow<List<TodoData>>
        get() = _todoList

    private var todoListCollectingJob: Job? = null

    override fun onResume(owner: LifecycleOwner) {
        todoListCollectingJob = viewModelScope.launch {
            getTodoListUseCase.execute()
                .onFailure {
                    eventChannel.send(Event.Error(it))
                }
                .onSuccess {
                    it.collect { list ->
                        _todoList.emit(list)
                    }
                }
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        todoListCollectingJob?.cancel()
        todoListCollectingJob = null
    }

    fun onRemoveTodo(todoData: TodoData) {
        viewModelScope.launch {
            removeTodoUseCase.execute(todoData)
                .onFailure { eventChannel.send(Event.Error(it)) }
                .onSuccess { eventChannel.send(Event.TodoRemoved(todoData)) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        eventChannel.close()
    }
}