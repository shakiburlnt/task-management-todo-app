package com.devin.todo.data

import android.content.Context
import android.content.SharedPreferences

actual class PlatformStorage(private val prefs: SharedPreferences) {
    actual fun load(key: String): String? = prefs.getString(key, null)

    actual fun save(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }
}

private lateinit var appContext: Context

fun initStorage(context: Context) {
    appContext = context.applicationContext
}

actual fun createPlatformStorage(): PlatformStorage {
    val prefs = appContext.getSharedPreferences("todo_prefs", Context.MODE_PRIVATE)
    return PlatformStorage(prefs)
}
