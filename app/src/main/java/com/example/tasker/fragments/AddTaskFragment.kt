package com.example.tasker.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.tasker.R
import com.example.tasker.api.NetworkManager
import com.example.tasker.categories.CategoriesData
import com.example.tasker.constants.Constants
import com.example.tasker.databinding.FragmentAddTaskBinding
import com.example.tasker.preferences.Preferences
import com.example.tasker.tasklist.TaskData
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class AddTaskFragment : Fragment() {

    private lateinit var binding: FragmentAddTaskBinding
    lateinit var networkManager: NetworkManager
    private var categoriesList = mutableListOf<CategoriesData>()
    private var categoryId: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        networkManager = NetworkManager(requireContext())
        binding = FragmentAddTaskBinding.inflate(inflater, container, false)

        binding.editTaskDate.setOnClickListener { showDatePicker() }

        binding.btnAddTask.setOnClickListener { createTask() }

        val one = CategoriesData("nombre", "123123")
        val two = CategoriesData("two", "2222")
        categoriesList.apply {
            add(one)
            add(two)
        }

        initSpinner()

        return binding.root

    }

    private fun initSpinner() {
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.categories_list,
            categoriesList
        )

        binding.spinnerCategories.adapter = adapter

        binding.spinnerCategories.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
                categoryId = categoriesList[i].id
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
    }

    //    opens datepicker
    private fun showDatePicker() {
        val datePicker = DatePickerFragment { day, month, year -> onSelectedDate(day, month, year) }
        datePicker.show(parentFragmentManager, "date picker")
    }

    //    updates ui with date
    private fun onSelectedDate(day: Int, month: Int, year: Int) {
        val myFormat = "MM/dd/yy"
        val dateFormat = SimpleDateFormat(myFormat, Locale.US)
        val date = Calendar.getInstance()
        date.set(Calendar.DAY_OF_MONTH, day)
        date.set(Calendar.MONTH, month)
        date.set(Calendar.YEAR, year)

        binding.editTaskDate.setText(dateFormat.format(date.time))
    }

    //    Create Task
    private fun createTask() {
        if (
            validate(binding.editTaskName) &&
            validate(binding.editTaskDescription) &&
            validate(binding.editTaskDate)
        ) {
            val task = TaskData(
                binding.editTaskName.text.toString(),
                binding.editTaskDescription.text.toString(),
                binding.editTaskDate.text.toString(),
                "62169a8647e84cb4af7f4e9a"
            )
            println(task)
            networkManager.createTask(requireContext(), parentFragmentManager, task)
//        volleyPostRequest()
        }
    }

    // show toast if edittext is empty
    private fun validate(editText: EditText): Boolean {
        return if (editText.text.isNotEmpty()) true
        else {
            Toast.makeText(requireContext(), "${editText.hint} required", Toast.LENGTH_SHORT).show()
            false
        }
    }

    // Volley request information
    private fun volleyPostRequest() {
        val token = Preferences(requireContext()).getToken()

        val queue = Volley.newRequestQueue(requireContext())
        val jsonBody = JSONObject().apply {
            put("todoName", binding.editTaskName.text)
            put("todoDescription", binding.editTaskDescription.text)
            put("dueDate", binding.editTaskDate.text)
            put("categoryId", "62169a8647e84cb4af7f4e9a")
        }
        val request = object : JsonObjectRequest(
            Method.POST, "${Constants.BASE_URL}/newtodo", jsonBody, {
                onSuccess()
            }, { error -> println(error) }
        ) {
            override fun getHeaders(): Map<String, String> {
                var params: MutableMap<String, String>? = HashMap()
                if (params == null) params = HashMap()
                params["Authorization"] = "Bearer $token"
                //..add other headers
                return params
            }
        }
        queue.add(request)
    }

    // Task created successfully
    private fun onSuccess() {
        val homeFragment = HomeFragment()
        Toast.makeText(requireContext(), "Task Created", Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed({
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.main_layout, homeFragment)
                addToBackStack(null)
                commit()
            }
        }, 1500)
    }
}