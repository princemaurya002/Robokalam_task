package com.princemaurya.robokalam.data

import android.content.Context
import android.content.SharedPreferences

class UserPreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveUser(email: String, password: String, name: String) {
        with(sharedPreferences.edit()) {
            putString(KEY_EMAIL, email)
            putString(KEY_PASSWORD, password)
            putString(KEY_NAME, name)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun getUserEmail(): String? {
        return sharedPreferences.getString(KEY_EMAIL, null)
    }

    fun getUserName(): String? {
        return sharedPreferences.getString(KEY_NAME, null)
    }

    fun validateUser(email: String, password: String): Boolean {
        val savedEmail = sharedPreferences.getString(KEY_EMAIL, null)
        val savedPassword = sharedPreferences.getString(KEY_PASSWORD, null)
        return email == savedEmail && password == savedPassword
    }

    fun logout() {
        sharedPreferences.edit().putBoolean(KEY_IS_LOGGED_IN, false).apply()
    }

    companion object {
        private const val PREF_NAME = "UserPrefs"
        private const val KEY_EMAIL = "email"
        private const val KEY_PASSWORD = "password"
        private const val KEY_NAME = "name"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }
} 