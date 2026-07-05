package com.devin.todo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.devin.todo.data.PlatformStorage
import com.devin.todo.data.TaskRepository
import com.devin.todo.data.createPlatformStorage
import com.devin.todo.ui.App
import com.devin.todo.ui.TaskViewModel

@Composable
fun AppRoot() {
    val viewModel = remember {
        TaskViewModel(TaskRepository(createPlatformStorage()))
    }
    App(viewModel)
}

fun buildViewModel(storage: PlatformStorage): TaskViewModel =
    TaskViewModel(TaskRepository(storage))
