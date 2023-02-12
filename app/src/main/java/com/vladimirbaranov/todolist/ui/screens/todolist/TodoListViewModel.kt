package com.vladimirbaranov.todolist.ui.screens.todolist

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladimirbaranov.todolist.domain.entity.TodoData
import com.vladimirbaranov.todolist.domain.usecases.CreateTodoUseCase
import com.vladimirbaranov.todolist.domain.usecases.GetTodoListUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class TodoListViewModel @Inject constructor(
    private val getTodoListUseCase: GetTodoListUseCase,
    private val createTodoUseCase: CreateTodoUseCase
) : ViewModel(), DefaultLifecycleObserver {

    private val _todoList = MutableStateFlow(listOf<TodoData>())

    val todoList: StateFlow<List<TodoData>>
        get() = _todoList

    private var todoListCollectingJob: Job? = null

    init {
        viewModelScope.launch { createTodoUseCase.execute("test") }
    }

    override fun onResume(owner: LifecycleOwner) {
        todoListCollectingJob = viewModelScope.launch {
            getTodoListUseCase.execute()
                .onFailure { }
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
}