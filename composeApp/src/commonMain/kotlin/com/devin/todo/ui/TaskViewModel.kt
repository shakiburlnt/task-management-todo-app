package com.devin.todo.ui

import androidx.lifecycle.ViewModel
import com.devin.todo.data.TaskRepository
import com.devin.todo.model.Priority
import com.devin.todo.model.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class TaskFilter(val label: String) {
    ALL("All"),
    ACTIVE("Active"),
    COMPLETED("Done")
}

enum class TaskSort(val label: String) {
    CREATED("Newest"),
    DUE_DATE("Due date"),
    PRIORITY("Priority"),
    ALPHABETICAL("A\u2013Z")
}

data class TaskUiState(
    val visibleTasks: List<Task> = emptyList(),
    val filter: TaskFilter = TaskFilter.ALL,
    val sort: TaskSort = TaskSort.CREATED,
    val query: String = "",
    val totalCount: Int = 0,
    val activeCount: Int = 0,
    val completedCount: Int = 0
)

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    private val filter = MutableStateFlow(TaskFilter.ALL)
    private val sort = MutableStateFlow(TaskSort.CREATED)
    private val query = MutableStateFlow("")

    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    init {
        recompute()
    }

    private fun recompute() {
        val all = repository.tasks.value
        val q = query.value.trim().lowercase()
        val filtered = all.filter { task ->
            val matchesFilter = when (filter.value) {
                TaskFilter.ALL -> true
                TaskFilter.ACTIVE -> !task.completed
                TaskFilter.COMPLETED -> task.completed
            }
            val matchesQuery = q.isEmpty() ||
                task.title.lowercase().contains(q) ||
                task.notes.lowercase().contains(q)
            matchesFilter && matchesQuery
        }
        val sorted = when (sort.value) {
            TaskSort.CREATED -> filtered.sortedByDescending { it.createdAt }
            TaskSort.DUE_DATE -> filtered.sortedWith(
                compareBy(nullsLast()) { it.dueDate }
            )
            TaskSort.PRIORITY -> filtered.sortedByDescending { it.priority.ordinal }
            TaskSort.ALPHABETICAL -> filtered.sortedBy { it.title.lowercase() }
        }
        _uiState.value = TaskUiState(
            visibleTasks = sorted,
            filter = filter.value,
            sort = sort.value,
            query = query.value,
            totalCount = all.size,
            activeCount = all.count { !it.completed },
            completedCount = all.count { it.completed }
        )
    }

    fun setFilter(value: TaskFilter) {
        filter.value = value
        recompute()
    }

    fun setSort(value: TaskSort) {
        sort.value = value
        recompute()
    }

    fun setQuery(value: String) {
        query.value = value
        recompute()
    }

    fun addTask(title: String, notes: String, priority: Priority, dueDate: Long?) {
        if (title.isBlank()) return
        repository.add(title, notes, priority, dueDate)
        recompute()
    }

    fun updateTask(task: Task) {
        repository.update(task)
        recompute()
    }

    fun deleteTask(id: String) {
        repository.delete(id)
        recompute()
    }

    fun toggleComplete(id: String) {
        repository.toggleComplete(id)
        recompute()
    }

    fun clearCompleted() {
        repository.clearCompleted()
        recompute()
    }
}
