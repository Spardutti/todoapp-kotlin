package com.example.tasker.tasklist

data class TaskData(
    val taskName: String,
    val taskDescription: String,
    val dueDate: String,
    val category: String,
    val categoryColor: String?,
    val id: String?
)

