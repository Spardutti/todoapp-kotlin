package com.example.tasker.api

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tasker.R
import com.example.tasker.databinding.FragmentHomeBinding
import com.example.tasker.fragments.HomeFragment
import com.example.tasker.preferences.Preferences
import com.example.tasker.tasklist.TaskAdapter
import com.example.tasker.tasklist.TaskData
import org.json.JSONArray
import org.json.JSONObject


class NetworkManager(val context: Context) {

    //        const val BASE_URL = "https://todo-app-lmdo.herokuapp.com/api"
    val BASE_URL = "http://10.0.2.2:5000/api"
    private val requestQueue = Volley.newRequestQueue(context)
    val token = Preferences(context).getToken()

    // Endpoints
    private val GET_TASKS = "/todos"
    private val NEW_TASK = "/newtodo"

    private fun newRequest(
        method: Int, url: String, body: JSONObject?, responseListener: Response.Listener<String>,
        errorListener: Response.ErrorListener
    ) {
        val request: StringRequest =
            object : StringRequest(method, url, responseListener, errorListener) {

                override fun getHeaders(): Map<String, String> {
                    var params: MutableMap<String, String>? = HashMap()
                    if (params == null) params = HashMap()
                    params["Authorization"] = "Bearer $token"
                    //..add other headers
                    return params
                }

                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String>? {
                    // Posting parameters to the url
                    val params: MutableMap<String, String> = HashMap()
                    params["tag"] = "generate"
                    if (body != null) {
                        params["todoName"] = body.getString("todoName")
                        params["todoDescription"] = body.getString("todoDescription")
                        params["categoryId"] = "62169a8647e84cb4af7f4e9a"
                        params["dueDate"] = body.getString("dueDate")
                    }

                    return params
                }
            }
        requestQueue.add(request)
    }

    private fun parseError(error: VolleyError) {
        if(error.networkResponse.data != null) {

            val body = String(error.networkResponse.data)
            val data = JSONObject(body)
            println(data)
            val message = data.optString("error")
            println(message)
        }
    }

    fun getTasks(taskList: MutableList<TaskData>, binding: FragmentHomeBinding) {
        newRequest(
            Request.Method.GET,
            BASE_URL + GET_TASKS,
            null,
            { response ->
                val responseArray = JSONArray(response)

                for (i in 0 until responseArray.length()) {
                    val task = responseArray.getJSONObject(i)
                    val taskName = task.getString("todoName")
                    val taskDescription = task.getString("todoDescription")
                    val listData = TaskData(taskName, taskDescription, "", "")
                    taskList.add(listData)
                    binding.recyclerView.adapter = TaskAdapter(taskList)
                }
            },
            { error ->
                parseError(error)
            })
    }

    fun createTask(context: Context, parentFragmentManager: FragmentManager, task: TaskData) {
        val body = JSONObject().apply {
            put("todoName", task.taskName)
            put("todoDescription", task.taskDescription)
            put("dueDate", task.dueDate)
            put("categoryId", task.category)
        }

        println(body)
        newRequest(Request.Method.POST, BASE_URL + NEW_TASK, body, { response ->
            val homeFragment = HomeFragment()
            Toast.makeText(context, "Task Created", Toast.LENGTH_SHORT).show()
            Handler(Looper.getMainLooper()).postDelayed({
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.main_layout, homeFragment)
                    addToBackStack(null)
                    commit()
                }
            }, 1500)
        }, { error ->
            println(body)
            parseError(error) })
    }

//
//    // Volley request information
//    private fun volleyPostRequest() {
//        val token = Preferences(requireContext()).getToken()
//
//        val queue = Volley.newRequestQueue(requireContext())
//        val jsonBody = JSONObject().apply {
//            put("todoName", binding.editTaskName.text)
//            put("todoDescription", binding.editTaskDescription.text)
//            put("dueDate", binding.editTaskDate.text)
//            put("categoryId", "62169a8647e84cb4af7f4e9a")
//        }
//        val request = object : JsonObjectRequest(
//            Method.POST, "${Constants.BASE_URL}/newtodo", jsonBody, {
//                onSuccess()
//            }, { error -> println(error) }
//        ) {
//            override fun getHeaders(): Map<String, String> {
//                var params: MutableMap<String, String>? = HashMap()
//                if (params == null) params = HashMap()
//                params["Authorization"] = "Bearer $token"
//                //..add other headers
//                return params
//            }
//        }
//        queue.add(request)
//    }
//
//    // Task created successfully
//    private fun onSuccess() {
//        val homeFragment = HomeFragment()
//        Toast.makeText(requireContext(), "Task Created", Toast.LENGTH_SHORT).show()
//        Handler(Looper.getMainLooper()).postDelayed({
//            parentFragmentManager.beginTransaction().apply {
//                replace(R.id.main_layout, homeFragment)
//                addToBackStack(null)
//                commit()
//            }
//        }, 1500)
//    }
}
