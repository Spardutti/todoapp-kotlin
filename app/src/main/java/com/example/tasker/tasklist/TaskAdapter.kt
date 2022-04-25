package com.example.tasker.tasklist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tasker.R
import com.example.tasker.api.NetworkManager
import com.example.tasker.databinding.TaskListBinding
import com.example.tasker.interfaces.RemoveTaskInterface
import kotlin.properties.Delegates

class TaskAdapter(private val taskList: MutableList<TaskData>) :
    RecyclerView.Adapter<TaskViewHolder>(),
    RemoveTaskInterface {
    var itemPosition: Int? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        itemPosition = viewType
        return TaskViewHolder(layoutInflater.inflate(R.layout.task_list, parent, false))
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val item = taskList[position]
        itemPosition = position
        holder.setOnTaskRemoved(this)
        holder.render(item)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onTaskRemove(task: TaskData) {
        taskList.removeAt(itemPosition!!)
        notifyDataSetChanged()
    }
}