package com.example.tasker.api

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.example.tasker.R
import com.example.tasker.categories.CategoriesData
import com.example.tasker.databinding.FragmentAddTaskBinding
import com.example.tasker.databinding.FragmentHomeBinding
import com.example.tasker.fragments.HomeFragment
import com.example.tasker.preferences.Preferences
import com.example.tasker.tasklist.TaskAdapter
import com.example.tasker.tasklist.TaskData
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit


class NetworkManager(val context: Context) {

    //        const val BASE_URL = "https://todo-app-lmdo.herokuapp.com/api"
    private val BASE_URL = "http://10.0.2.2:5000"
    private val token = Preferences(context).getToken()

    /*Redirect to home fragment after task created*/
    private fun redirectToHomeFragment(parentFragmentManager: FragmentManager) {
        val homeFragment = HomeFragment()
        Toast.makeText(context, "Task Created", Toast.LENGTH_SHORT).show()
        Handler(Looper.getMainLooper()).postDelayed({
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.main_layout, homeFragment)
                addToBackStack(null)
                commit()
            }
        }, 1500)
    }

    /* makes the retrofit response readable */
    private fun prettifyJson(response: retrofit2.Response<ResponseBody>): String {
        val gson = GsonBuilder().setPrettyPrinting().create()

        return gson.toJson(
            JsonParser.parseString(
                response.body()
                    ?.string()
            )
        )
    }

    /* dry */
    private fun createRetrofitService(): APIService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL).build()

        return retrofit.create(APIService::class.java)
    }

    /*Creates a new task */
    fun newTask(task: TaskData, parentFragmentManager: FragmentManager) {

        val service = createRetrofitService()
        val jsonObject = JSONObject().apply {
            put("todoName", task.taskName)
            put("todoDescription", task.taskDescription)
            put("dueDate", task.dueDate)
            put("categoryId", task.category)
        }

        val jsonObjectString = jsonObject.toString()

        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.createTask("Bearer $token", requestBody)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    redirectToHomeFragment(parentFragmentManager)
                } else {
                    println(response.code().toString())
                }
            }
        }
    }

    /* Local login */
    fun localLogin() {
        val service = createRetrofitService()
        val jsonObject = JSONObject().apply { }
    }


    /*Get current user today tasks */
    fun getTasks(taskList: MutableList<TaskData>, binding: FragmentHomeBinding) {
        val service = createRetrofitService()

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getUserTasks("Bearer $token")

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val responseArray = JSONArray(prettifyJson(response))
                    for (i in 0 until responseArray.length()) {
                        val task = responseArray.getJSONObject(i)
                        val taskName = task.getString("todoName")
                        val taskDescription = task.getString("todoDescription")
                        val taskId = task.getString("_id")
                        val taskCategory = task.getJSONObject("category")
                        val categoryName = taskCategory.getString("categoryName")
                        val categoryColor = taskCategory.getString("color")
                        val listData =
                            TaskData(taskName, taskDescription, "", categoryName, categoryColor, taskId)
                        taskList.add(listData)
                        binding.recyclerViewToday.adapter = TaskAdapter(taskList)
                    }
                } else {
                    println(response.code().toString())
                }
            }
        }
    }

    /* get current user overdue tasks */
    fun getOverdueTasks(taskList: MutableList<TaskData>, binding: FragmentHomeBinding) {
        val service = createRetrofitService()

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getOverdueTasks("Bearer $token")

            withContext(Dispatchers.Main){
                val responseArray = JSONArray(prettifyJson(response))
                if(responseArray.length() == 0) {
                    binding.textOverduetodos.visibility = View.GONE
                    binding.recyclerViewOverdue.visibility = View.GONE
                } else {
                    for( i in 0 until responseArray.length()){
                        val task = responseArray.getJSONObject(i)
                        val taskName = task.getString("todoName")
                        val taskDescription = task.getString("todoDescription")
                        val taskId = task.getString("_id")
                        val taskCategory = task.getJSONObject("category")
                        val categoryName = taskCategory.getString("categoryName")
                        val categoryColor = taskCategory.getString("color")
                        val listData =
                            TaskData(taskName, taskDescription, "", categoryName, categoryColor, taskId)
                        taskList.add(listData)
                        binding.recyclerViewOverdue.adapter = TaskAdapter(taskList)
                    }
                }
            }
        }
    }

    /* Get a list of user categories. Required to create a new task */
    fun getUserCategories(
        categoriesList: MutableList<CategoriesData>,
        binding: FragmentAddTaskBinding
    ) {
        val service = createRetrofitService()
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getUserCategories("Bearer $token")

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val responseArray = JSONArray(prettifyJson(response))
                    for (i in 0 until responseArray.length()) {
                        val task = responseArray.getJSONObject(i)
                        val categoryName = task.getString("categoryName")
                        val categoryId = task.getString("_id")
                        val listData = CategoriesData(categoryName, categoryId)
                        categoriesList.add(listData)
//                        binding.spinnerCategories.adapter = TaskAdapter(taskList)
                    }
                } else println(response.code().toString())
            }
        }
    }

    /* Delete task by id */
    fun deleteTask(id: String) {
        val service = createRetrofitService()

        CoroutineScope(Dispatchers.IO).launch {
            val response = service.deleteTask(id, "Bearer $token")

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    println(prettifyJson(response))

                } else {
                    println(response.code().toString())
                }
            }
        }
    }
}
