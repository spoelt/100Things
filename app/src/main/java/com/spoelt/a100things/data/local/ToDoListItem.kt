package com.spoelt.a100things.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "to_do_list_items")
data class ToDoListItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    var title: String,
    var urgency: Int,
    var dueDate: String,
    var isComplete: Boolean
)