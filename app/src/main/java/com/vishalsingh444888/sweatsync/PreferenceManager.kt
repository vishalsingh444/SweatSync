package com.vishalsingh444888.sweatsync

import android.content.Context

class PreferenceManager(private val context: Context) {

    private val sharedPreferences = context.getSharedPreferences("app_prefs",Context.MODE_PRIVATE)

    var isAuthenticated: Boolean
        get() = sharedPreferences.getBoolean("is_authenticated",false)
        set(value) = sharedPreferences.edit().putBoolean("is_authenticated",value).apply()
}