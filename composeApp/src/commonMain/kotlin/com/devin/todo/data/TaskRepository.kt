package com.devin.todo.data

import com.devin.todo.model.Priority
import com.devin.todo.model.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class TaskRepository(private val storage: PlatformStorage) {

    private val json = Json { ignoreUnknownKeys = true }
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    init {
        val raw = storage.load(STORAGE_KEY)
        if (raw.isNullOrBlank()) {
            _tasks.value = seedTasks()
            persist()
        } else {
            _tasks.value = runCatching { json.decodeFromString<List<Task>>(raw) }
                .getOrElse { seedTasks() }
        }
    }

    fun add(title: String, notes: String, priority: Priority, dueDate: Long?) {
        val task = Task(
            id = randomId(),
            title = title.trim(),
            notes = notes.trim(),
            priority = priority,
            dueDate = dueDate,
            completed = false,
            createdAt = nowMillis()
        )
        _tasks.value = _tasks.value + task
        persist()
    }

    fun update(task: Task) {
        _tasks.value = _tasks.value.map { if (it.id == task.id) task else it }
        persist()
    }

    fun delete(id: String) {
        _tasks.value = _tasks.value.filterNot { it.id == id }
        persist()
    }

    fun toggleComplete(id: String) {
        _tasks.value = _tasks.value.map {
            if (it.id == id) it.copy(completed = !it.completed) else it
        }
        persist()
    }

    fun clearCompleted() {
        _tasks.value = _tasks.value.filterNot { it.completed }
        persist()
    }

    private fun persist() {
        storage.save(STORAGE_KEY, json.encodeToString(_tasks.value))
    }

    private fun seedTasks(): List<Task> {
        val now = nowMillis()
        val day = 24L * 60 * 60 * 1000
        return listOf(
            Task(randomId(), "Welcome to Tasks", "Tap a task to edit it. Check the box to complete.", Priority.MEDIUM, now + day, false, now),
            Task(randomId(), "Plan the week", "Break big goals into small todos.", Priority.HIGH, now + 2 * day, false, now - 1000),
            Task(randomId(), "Buy groceries", "Milk, eggs, coffee.", Priority.LOW, null, true, now - 2000)
        )
    }

    companion object {
        private const val STORAGE_KEY = "todo_tasks_v1"
    }
}
