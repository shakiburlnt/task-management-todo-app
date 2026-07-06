package com.devin.todo

import android.app.Application
import com.devin.todo.data.initStorage

class TodoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initStorage(this)
    }
}
