package com.devin.todo.ui

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

private val monthNames = listOf(
    "Jan", "Feb", "Mar", "Apr", "May", "Jun",
    "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
)

fun formatDueDate(millis: Long): String {
    val dt = Instant.fromEpochMilliseconds(millis)
        .toLocalDateTime(TimeZone.currentSystemDefault())
    val month = monthNames[dt.monthNumber - 1]
    return "$month ${dt.dayOfMonth}, ${dt.year}"
}
