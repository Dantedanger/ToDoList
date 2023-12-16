package com.sample.todolist

import com.sample.todolist.database.ListDatabase

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import java.util.UUID
import java.util.concurrent.Executors

private const val DATABASE_NAME = "list"
class ListRepository private constructor(context: Context) {
    private val database: ListDatabase = Room
        .databaseBuilder(
            context.applicationContext,
            ListDatabase::class.java,
            DATABASE_NAME
        )
        .build()
    private val listDao = database.listDao()
    private val executor = Executors.newSingleThreadExecutor()
    fun getLists(): LiveData<List<ListItem>> = listDao.getLists()

    fun getList(id: UUID): LiveData<ListItem?> = listDao.getList(id)

    fun addList(list: ListItem) {
        executor.execute {
            listDao.addList(list)
        }
    }

    fun updateList(list: ListItem) {
        executor.execute {
            listDao.updateList(list)
        }
    }
    fun deleteList(id: UUID) {
        executor.execute {
            listDao.deleteList(id)
        }
    }

    companion object {
        private var INSTANCE: ListRepository? = null
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE =
                    ListRepository(context)
            }
        }
        fun get(): ListRepository {
            return INSTANCE ?:
            throw
            IllegalStateException("ListRepository must be initialized")
        }
    }

}
