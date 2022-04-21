package com.example.tasker.preferences

import android.content.Context
import android.content.SharedPreferences
import com.example.tasker.constants.Constants

class Preferences(private val context: Context) {
    private val storage: SharedPreferences = context.getSharedPreferences(Constants.TASKER_PREF, 0)

    fun saveToken(token: String) {
        storage.edit().putString(Constants.TASKER_TOKEN, token).apply()
    }

    fun getToken(): String? {
        return storage.getString(Constants.TASKER_TOKEN, null)
    }
}