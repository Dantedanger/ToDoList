package com.sample.todolist

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "list")
data class ListItem(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    var title: String = "",
    var priority: String = "")
