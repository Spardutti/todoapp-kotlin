package com.example.tasker.tasklist

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.tasker.databinding.TaskListBinding

class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = TaskListBinding.bind(view)

    fun render(taskList: TaskData) {
        binding.cardTaskName.text = taskList.taskName
        binding.cardTaskDescription.text = taskList.taskDescription
    }
}