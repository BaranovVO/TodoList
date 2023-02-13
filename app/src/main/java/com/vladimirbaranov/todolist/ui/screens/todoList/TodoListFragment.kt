package com.vladimirbaranov.todolist.ui.screens.todoList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.vladimirbaranov.todolist.R
import com.vladimirbaranov.todolist.databinding.FragmentTodoListBinding
import com.vladimirbaranov.todolist.domain.entity.TodoData
import com.vladimirbaranov.todolist.ui.adapters.TodoListAdapter
import com.vladimirbaranov.todolist.ui.screens.addTodo.AddTodoFragment
import com.vladimirbaranov.todolist.ui.screens.todoList.TodoListViewModel.Event.Error
import com.vladimirbaranov.todolist.ui.screens.todoList.TodoListViewModel.Event.TodoRemoved
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
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
        configFab()
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

        viewModel.todoList.flowWithLifecycle(
            lifecycle = viewLifecycleOwner.lifecycle,
            minActiveState = Lifecycle.State.STARTED
        )
            .onEach {
                viewModel.todoList.collect {
                    todoListAdapter.updateTodoList(it)
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun configFab() {
        binding?.fab?.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.contentContainer, AddTodoFragment())
                .addToBackStack(null)
                .commit()
            requireActivity().supportFragmentManager.executePendingTransactions()
        }
    }

    private fun configEvents() {
        viewModel.events.flowWithLifecycle(
            lifecycle = viewLifecycleOwner.lifecycle,
            minActiveState = Lifecycle.State.STARTED
        )
            .onEach {
                when (it) {
                    is Error -> onErrorEvent(it)
                    is TodoRemoved -> onTodoRemovedEvent(it)
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun onErrorEvent(error: Error) {
        Toast.makeText(requireContext(), getString(R.string.error), Toast.LENGTH_SHORT).show()
    }

    private fun onTodoRemovedEvent(todoRemoved: TodoRemoved) {
        Toast.makeText(requireContext(), getString(R.string.removed), Toast.LENGTH_SHORT).show()
    }
}