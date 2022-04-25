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
import com.example.tasker.R
import com.example.tasker.api.NetworkManager
import com.example.tasker.categories.CategoriesData
import com.example.tasker.constants.Constants
import com.example.tasker.databinding.FragmentAddTaskBinding
import com.example.tasker.preferences.Preferences
import com.example.tasker.tasklist.TaskData
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


class AddTaskFragment : Fragment() {

    private lateinit var binding: FragmentAddTaskBinding
    lateinit var networkManager: NetworkManager
    private var categoriesList = mutableListOf<CategoriesData>()
    private var categoryId: String = ""
    lateinit var dateTime: Date

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        networkManager = NetworkManager(requireContext())
        binding = FragmentAddTaskBinding.inflate(inflater, container, false)

        binding.editTaskDate.setOnClickListener { showDatePicker() }

        binding.btnAddTask.setOnClickListener { createTask() }

        val one = CategoriesData("Please Select a Category", "123123")
        categoriesList.apply {
            add(one)
        }
        getUserCategories()

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
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                i: Int,
                l: Long
            ) {
                if (i == 0) binding.spinnerCategories.setSelection(0, false)
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
        dateTime = date.time
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
                dateTime.toString(),
                categoryId,
                null,
                null
            )
            networkManager.newTask(task, parentFragmentManager)
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

    /* Get user categories to display in dropdown */
    private fun getUserCategories() {
        networkManager.getUserCategories(categoriesList, binding)
    }
}