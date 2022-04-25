package com.example.tasker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tasker.api.NetworkManager
import com.example.tasker.databinding.FragmentHomeBinding
import com.example.tasker.tasklist.TaskAdapter
import com.example.tasker.tasklist.TaskData


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private var taskList = mutableListOf<TaskData>()
    private var overdueTasks = mutableListOf<TaskData>()
    private lateinit var networkManager: NetworkManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        networkManager = NetworkManager(requireContext())
        initRecyclerView()

        initOverduesRecycler()

        return binding.root
    }

    private fun initRecyclerView() {
        binding.recyclerViewToday.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewToday.adapter = TaskAdapter(taskList)
        networkManager.getTasks(taskList, binding)
    }

    private fun initOverduesRecycler() {
        binding.recyclerViewOverdue.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewOverdue.adapter = TaskAdapter(overdueTasks)
        networkManager.getOverdueTasks(overdueTasks, binding)
    }
}