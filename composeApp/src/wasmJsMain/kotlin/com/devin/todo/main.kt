package com.devin.todo

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    document.getElementById("loading")?.remove()
    val container = document.getElementById("app") ?: document.body!!
    ComposeViewport(container) {
        AppRoot()
    }
}
