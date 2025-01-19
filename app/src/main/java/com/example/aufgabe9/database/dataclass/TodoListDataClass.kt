package com.example.aufgabe9.database.dataclass

data class TodoListDataClass (
    val id: Int = 0,
    val name: String,
    val description: String,
    val priority: Priority,
    val deadline: String? = null,
    val status: Int = 0
)