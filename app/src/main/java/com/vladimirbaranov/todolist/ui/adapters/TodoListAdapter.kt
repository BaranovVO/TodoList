package com.vladimirbaranov.todolist.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.vladimirbaranov.todolist.databinding.TodoItemCardBinding
import com.vladimirbaranov.todolist.domain.entity.TodoData
import com.vladimirbaranov.todolist.ui.adapters.TodoListAdapter.ViewHolder

class TodoListAdapter(
    private val todoItemListener: TodoItemListener
) :
    RecyclerView.Adapter<ViewHolder>() {

    private val items = mutableListOf<TodoData>()

    fun updateTodoList(newList: List<TodoData>) {
        val diffUtilCallback = TodoListDiffUtilCallback(items, newList)
        val result = DiffUtil.calculateDiff(diffUtilCallback)
        items.clear()
        items.addAll(newList)
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            TodoItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            todoItemListener
        )
    }

    override fun getItemCount(): Int = items.count()

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class ViewHolder(
        private val binding: TodoItemCardBinding,
        private val todoItemListener: TodoItemListener
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(todoData: TodoData) {
            binding.todoText.text = todoData.text
            binding.btRemove.setOnClickListener { todoItemListener.onRemove(todoData) }
        }
    }

    private class TodoListDiffUtilCallback(
        private val oldList: List<TodoData>,
        private val newList: List<TodoData>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    interface TodoItemListener {
        fun onRemove(todoData: TodoData)
    }
}