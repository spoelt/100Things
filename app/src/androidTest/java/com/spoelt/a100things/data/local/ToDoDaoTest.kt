package com.spoelt.a100things.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.spoelt.a100things.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
// annotation makes sure that all instrumented tests will run on the emulator
@SmallTest
// to tell JUnit that these tests are unit tests
class ToDoDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: ToDoListDatabase
    private lateinit var dao: ToDoDao

    @Before
    fun setup() {
        // when using Room.inMemoryDatabaseBuilder, data will not be saved persistently
        // but just for the test
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ToDoListDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.toDoDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    // runBlockingTest runs a new coroutine and blocks the current thread until it's completed.
    // It will also skip any delays or sleeps in the code.
    fun insertToDoListItem() = runBlockingTest {
        val item = ToDoListItem(1, "TestItem 1", 2, "01.01.2020", false)
        dao.insert(item)

        val openItems = dao.observeOpenToDoListItems().getOrAwaitValue()

        assertThat(openItems).contains(item)
    }

    @Test
    fun deleteToDoListItem() = runBlockingTest {
        val item = ToDoListItem(1, "TestItem 1", 2, "01.01.2020", false)
        dao.insert(item)
        dao.delete(item)

        val openItems = dao.observeOpenToDoListItems().getOrAwaitValue()

        assertThat(openItems).doesNotContain(item)
    }

    @Test
    fun updateToDoListItem() = runBlockingTest {
        val item = ToDoListItem(1, "TestItem 1", 2, "01.01.2020", false)
        dao.insert(item)

        item.urgency = 3
        dao.update(item)

        val openItems = dao.observeOpenToDoListItems().getOrAwaitValue()

        assertThat(openItems.first { it.id == item.id }.urgency).isEqualTo(item.urgency)
    }

    @Test
    fun observeOpenToDoListItems() = runBlockingTest {
        val item1 = ToDoListItem(1, "TestItem 1", 2, "01.01.2020", false)
        val item2 = ToDoListItem(2, "TestItem 2", 1, "01.02.2020", false)
        val item3 = ToDoListItem(3, "TestItem 3", 3, "01.03.2020", true)

        dao.insert(item1)
        dao.insert(item2)
        dao.insert(item3)

        val openItems = dao.observeOpenToDoListItems().getOrAwaitValue()

        assertThat(openItems.size).isEqualTo(2)
    }

    @Test
    fun observeCompletedToDoListItems() = runBlockingTest {
        val item1 = ToDoListItem(1, "TestItem 1", 2, "01.01.2020", false)
        val item2 = ToDoListItem(2, "TestItem 2", 1, "01.02.2020", false)
        val item3 = ToDoListItem(3, "TestItem 3", 3, "01.03.2020", true)

        dao.insert(item1)
        dao.insert(item2)
        dao.insert(item3)

        val completedItems = dao.observeCompletedToDoListItems().getOrAwaitValue()

        assertThat(completedItems.size).isEqualTo(1)
    }
}