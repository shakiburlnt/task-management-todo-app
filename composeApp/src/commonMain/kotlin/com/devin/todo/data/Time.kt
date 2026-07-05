package com.devin.todo.data

import kotlinx.datetime.Clock

fun nowMillis(): Long = Clock.System.now().toEpochMilliseconds()

fun randomId(): String {
    val chars = "abcdefghijklmnopqrstuvwxyz0123456789"
    val builder = StringBuilder()
    repeat(16) { builder.append(chars.random()) }
    return builder.toString() + "-" + nowMillis().toString()
}
