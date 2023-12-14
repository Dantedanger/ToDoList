package com.sample.todolist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.util.UUID

class ListDetailViewModel(): ViewModel(){
    private val listRepository = ListRepository.get()
    private val listIdLiveData = MutableLiveData<UUID>()
    var listLiveData: LiveData<ListItem?> =
        Transformations.switchMap(listIdLiveData) { listId ->
            listRepository.getList(listId)
        }
    fun loadList(listId: UUID) {
        listIdLiveData.value = listId
    }

    fun saveList(list: ListItem) {
        listRepository.updateList(list)
    }
}