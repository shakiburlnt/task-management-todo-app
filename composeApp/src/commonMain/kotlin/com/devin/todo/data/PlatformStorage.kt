package com.devin.todo.data

/**
 * Simple key/value persistence abstraction backed by the platform's
 * native storage (SharedPreferences on Android, localStorage on Web).
 */
expect class PlatformStorage {
    fun load(key: String): String?
    fun save(key: String, value: String)
}

expect fun createPlatformStorage(): PlatformStorage
