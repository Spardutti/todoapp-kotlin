package com.example.tasker.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.tasker.R
import com.example.tasker.api.NetworkManager
import com.example.tasker.constants.Constants
import com.example.tasker.databinding.FragmentHomeBinding
import com.example.tasker.preferences.Preferences
import com.example.tasker.tasklist.TaskAdapter
import com.example.tasker.tasklist.TaskData
import org.json.JSONArray


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private var taskList = mutableListOf<TaskData>()
    private lateinit var networkManager: NetworkManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        networkManager = NetworkManager(requireContext())
        initRecyclerView()

        return binding.root
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = TaskAdapter(taskList)

//        get current user tasks
        networkManager.getTasks(taskList, binding)
    }
}