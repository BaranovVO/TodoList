package com.vladimirbaranov.todolist.ui.screens.addTodo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.vladimirbaranov.todolist.R
import com.vladimirbaranov.todolist.databinding.FragmentAddTodoBinding
import com.vladimirbaranov.todolist.ui.screens.addTodo.AddTodoViewModel.Event.Error
import com.vladimirbaranov.todolist.ui.screens.addTodo.AddTodoViewModel.Event.TodoAdded
import dagger.android.support.DaggerFragment
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class AddTodoFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: AddTodoViewModel by viewModels { viewModelFactory }

    private var binding: FragmentAddTodoBinding? = null

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
        val binding = FragmentAddTodoBinding.inflate(inflater, container, false)
        this.binding = binding
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configTodoEditAndButton()
        configEvents()
    }

    private fun configTodoEditAndButton() {
        binding?.apply {
            etTodo.addTextChangedListener { editable ->
                editable?.let { todoText ->
                    btAdd.isEnabled = todoText.isNotBlank()
                }
            }
            btAdd.isEnabled = etTodo.text?.isNotBlank() ?: false
            btAdd.setOnClickListener {
                etTodo.text?.let { viewModel.addTodo(it.toString()) }
            }
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
                    is TodoAdded -> onAddedEvent(it)
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun onAddedEvent(todoAdded: TodoAdded) {
        requireActivity().supportFragmentManager.popBackStack()
    }

    private fun onErrorEvent(error: Error) {
        Toast.makeText(requireContext(), getString(R.string.error), Toast.LENGTH_SHORT).show()
    }
}