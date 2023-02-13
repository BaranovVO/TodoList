package com.vladimirbaranov.todolist.ui.screens.addTodo

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
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

    override fun onResume() {
        super.onResume()
        binding?.etTodo?.apply {
            requestFocus()
            showKeyboard()
        }
    }

    private fun configTodoEditAndButton() {
        binding?.apply {
            etTodo.addTextChangedListener { editable ->
                editable?.let { todoText ->
                    btAdd.isEnabled = todoText.isNotBlank()
                }
            }
            etTodo.setOnEditorActionListener { _, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    etTodo.text?.let { viewModel.addTodo(it.toString()) }
                    true
                } else {
                    false
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
        Toast.makeText(requireContext(), getString(R.string.todoAdded), Toast.LENGTH_SHORT).show()
        requireActivity().supportFragmentManager.popBackStack()
    }

    private fun onErrorEvent(error: Error) {
        Toast.makeText(requireContext(), getString(R.string.error), Toast.LENGTH_SHORT).show()
    }

    private fun View.showKeyboard() {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(this, 0)
    }
}