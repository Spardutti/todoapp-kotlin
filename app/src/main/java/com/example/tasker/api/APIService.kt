package com.example.tasker.api

import okhttp3.*
import okhttp3.Interceptor.*
import retrofit2.http.*


interface APIService {

    @POST("/api/newtodo")
    suspend fun createTask(
        @Header("Authorization") token: String,
        @Body requestBody: RequestBody
    ): retrofit2.Response<ResponseBody>

    @POST("/api/localuser")
    suspend fun localLogin(
        @Header("Authorization") token: String,
        @Body requestBody: RequestBody
    ): retrofit2.Response<ResponseBody>

    /* today tasks */
    @GET("/api/todaystodos")
    suspend fun getUserTasks(@Header("Authorization") token: String): retrofit2.Response<ResponseBody>

    /* overdue tasks */
    @GET("/api/overduetodos")
    suspend fun getOverdueTasks(@Header("Authorization") token: String): retrofit2.Response<ResponseBody>

    /* user categories */
    @GET("api/categories")
    suspend fun getUserCategories(@Header("Authorization") token: String): retrofit2.Response<ResponseBody>

    /* delete task */
    @DELETE("api/todo/{id}")
    suspend fun deleteTask(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): retrofit2.Response<ResponseBody>
}