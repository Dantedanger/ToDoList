package com.sample.todolist

import androidx.lifecycle.ViewModel
import com.sample.todolist.ListItem
import com.sample.todolist.ListRepository
import java.util.UUID

class ListViewModel : ViewModel() {
    private val listRepository = ListRepository.get()
    val ListLiveData  = listRepository.getLists()
    fun addList(list: ListItem) {
        listRepository.addList(list)
    }
    fun deleteList(listId: UUID) {
        listRepository.deleteList(listId)
    }
}