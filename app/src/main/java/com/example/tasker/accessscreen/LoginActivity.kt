package com.example.tasker.accessscreen

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tasker.constants.Constants
import com.example.tasker.databinding.ActivityLoginBinding
import com.example.tasker.preferences.Preferences
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        binding.testButton.setOnClickListener { testUserLogin() }
//        binding.btnLogin.setOnClickListener { localLogin() }

    }



//    //    extract the token from the response
//    private fun extractJson(response: JSONObject) {
//        val token = response.getString("token")
//        Preferences(this).saveToken(token)
//    }
//
//    //    local login
//    private fun localLogin() {
//        if (binding.editEmail.text.toString().isNotEmpty() && binding.editPassword.text.toString()
//                .isNotEmpty()
//        ) {
//            val queue = Volley.newRequestQueue(this)
//            val jsonBody = JSONObject()
//            val email = binding.editEmail.text.toString()
//            val password = binding.editPassword.text.toString()
//            jsonBody.put("email", email)
//            jsonBody.put("password", password)
//
//            val request = JsonObjectRequest(
//                Request.Method.POST,
//                "${Constants.BASE_URL}/localuser",
//                jsonBody,
//                { response ->
//                    try {
//                        extractJson(response)
//
//                    } catch (ex: Exception) {
//                        println(ex)
//                    }
//                }) { error -> parseError(error) }
//            queue.add(request)
//        } else {
//            Toast.makeText(this, "Please enter your username and password", Toast.LENGTH_SHORT)
//                .show()
//        }
//    }
//
//    //    extract the error message from the response
//    private fun parseError(error: VolleyError) {
//        if (error.networkResponse.data != null) {
//            val body = String(error.networkResponse.data)
//            val data = JSONObject(body)
//            val message = data.optString("error")
//            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//        }
//    }
}

