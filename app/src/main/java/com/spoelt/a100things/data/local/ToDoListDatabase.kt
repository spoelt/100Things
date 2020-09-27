package com.spoelt.a100things.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ToDoListItem::class],
    version = 1
)
abstract class ToDoListDatabase : RoomDatabase() {

    abstract fun toDoDao(): ToDoDao
}