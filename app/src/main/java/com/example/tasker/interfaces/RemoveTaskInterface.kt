package com.example.tasker.interfaces

import com.example.tasker.tasklist.TaskData

interface RemoveTaskInterface {
    fun onTaskRemove(task: TaskData)
}