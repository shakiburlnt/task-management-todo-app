package com.devin.todo.data

import kotlinx.browser.localStorage
import org.w3c.dom.get
import org.w3c.dom.set

actual class PlatformStorage {
    actual fun load(key: String): String? = localStorage[key]

    actual fun save(key: String, value: String) {
        localStorage[key] = value
    }
}

actual fun createPlatformStorage(): PlatformStorage = PlatformStorage()
