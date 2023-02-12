package com.vladimirbaranov.todolist.data.repository

import com.vladimirbaranov.todolist.data.database.TodoDao
import com.vladimirbaranov.todolist.data.database.TodoEntity
import com.vladimirbaranov.todolist.domain.entity.TodoData
import com.vladimirbaranov.todolist.domain.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class TodoRepositoryImpl(
    private val todoDao: TodoDao
) : TodoRepository {

    override fun getTodoList(): Flow<List<TodoData>> {
        return todoDao.getAll().map { list -> list.map { it.toData() } }
    }

    override suspend fun createTodo(text: String): TodoData =
        withContext(Dispatchers.IO) {
            val rowId = todoDao.insert(TodoEntity(id = 0, text = text))
            return@withContext todoDao.getByRowId(rowId).toData()
        }

    override suspend fun removeTodo(todoData: TodoData) =
        withContext(Dispatchers.IO) {
            todoDao.deleteById(todoData.id)
        }

    private fun TodoEntity.toData(): TodoData {
        return TodoData(
            id = this.id,
            text = this.text
        )
    }

    private fun TodoData.toEntity(): TodoEntity {
        return TodoEntity(
            id = this.id,
            text = this.text
        )
    }
}