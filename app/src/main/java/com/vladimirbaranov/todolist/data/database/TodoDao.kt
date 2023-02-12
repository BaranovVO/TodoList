package com.vladimirbaranov.todolist.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("SELECT * FROM Todo")
    fun getAll(): Flow<List<TodoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todoEntity: TodoEntity): Long

    @Update
    suspend fun update(todoEntity: TodoEntity)

    @Query("SELECT * FROM Todo WHERE rowid = :rowid")
    suspend fun getByRowId(rowid: Long): TodoEntity

    @Query("DELETE FROM Todo WHERE id = :id")
    suspend fun deleteById(id: Int)
}