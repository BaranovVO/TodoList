package com.vladimirbaranov.todolist.ui.screens.todoList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.vladimirbaranov.todolist.databinding.FragmentTodoListBinding
import com.vladimirbaranov.todolist.domain.entity.TodoData
import com.vladimirbaranov.todolist.ui.adapters.TodoListAdapter
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class TodoListFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: TodoListViewModel by viewModels { viewModelFactory }

    private var binding: FragmentTodoListBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(viewModel)
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentTodoListBinding.inflate(inflater, container, false)
        this.binding = binding
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configTodoList()
        configEvents()
    }

    private fun configTodoList() {
        val todoListAdapter = TodoListAdapter(object : TodoListAdapter.TodoItemListener {
            override fun onRemove(todoData: TodoData) {
                viewModel.onRemoveTodo(todoData)
            }
        })

        binding?.rvTodoList?.apply {
            adapter = todoListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        lifecycleScope.launchWhenStarted {
            viewModel.todoList.collect {
                todoListAdapter.updateTodoList(it)
            }
        }
    }

    private fun configEvents() {
        viewModel.events.flowWithLifecycle(
            lifecycle = viewLifecycleOwner.lifecycle,
            minActiveState = Lifecycle.State.STARTED
        )
            .onEach { }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }
}