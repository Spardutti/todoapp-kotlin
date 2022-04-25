package com.example.tasker.tasklist

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.tasker.api.NetworkManager
import com.example.tasker.databinding.TaskListBinding
import com.example.tasker.interfaces.RemoveTaskInterface

class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = TaskListBinding.bind(view)
    var networkManager: NetworkManager = NetworkManager(view.context)
    lateinit var removeTaskInt: RemoveTaskInterface

    fun render(taskList: TaskData) {
        binding.cardTaskName.text = taskList.taskName
        binding.cardTaskDescription.text = taskList.taskDescription
        binding.textCategoryName.text = taskList.category
        binding.textCategoryName.setTextColor(Color.parseColor(taskList.categoryColor))


        binding.imageDeleteTask.setOnClickListener {
            networkManager.deleteTask(taskList.id!!)
            removeTaskInt.onTaskRemove(taskList)
        }
    }

    fun setOnTaskRemoved(removeTaskInterface: RemoveTaskInterface) {
        removeTaskInt = removeTaskInterface
    }
}