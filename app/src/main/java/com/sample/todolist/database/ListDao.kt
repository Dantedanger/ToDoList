package com.sample.todolist.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import java.util.UUID
import com.sample.todolist.ListItem

@Dao
interface ListDao {
    @Query("SELECT * FROM list")
    fun getLists(): LiveData<List<ListItem>>
    @Query("SELECT * FROM list WHERE id=(:id)")
    fun getList(id: UUID): LiveData<ListItem?>

    @Insert
    fun addList(list: ListItem)

}