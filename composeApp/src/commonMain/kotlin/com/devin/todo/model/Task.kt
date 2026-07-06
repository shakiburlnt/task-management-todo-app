package com.devin.todo.model

import kotlinx.serialization.Serializable

enum class Priority(val label: String) {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High")
}

@Serializable
data class Task(
    val id: String,
    val title: String,
    val notes: String = "",
    val priority: Priority = Priority.MEDIUM,
    val dueDate: Long? = null,
    val completed: Boolean = false,
    val createdAt: Long = 0L
)
