package com.spoelt.a100things.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ToDoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(toDoListItem: ToDoListItem)

    @Update
    suspend fun update(toDoListItem: ToDoListItem)

    @Delete
    suspend fun delete(toDoListItem: ToDoListItem)

    @Query("SELECT * FROM to_do_list_items WHERE isComplete = 1")
    fun observeCompletedToDoListItems(): LiveData<List<ToDoListItem>>

    @Query("SELECT * FROM to_do_list_items WHERE isComplete = 0")
    fun observeOpenToDoListItems(): LiveData<List<ToDoListItem>>
}