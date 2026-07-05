package com.devin.todo.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF4C5DF0),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0E3FF),
    onPrimaryContainer = Color(0xFF10154B),
    secondary = Color(0xFF5B6072),
    background = Color(0xFFF7F8FC),
    surface = Color.White,
    surfaceVariant = Color(0xFFEDEEF5),
    onSurface = Color(0xFF1A1B22),
    onSurfaceVariant = Color(0xFF5B6072)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFB8C0FF),
    onPrimary = Color(0xFF1B2270),
    primaryContainer = Color(0xFF333C8C),
    onPrimaryContainer = Color(0xFFE0E3FF),
    secondary = Color(0xFFC3C6D6),
    background = Color(0xFF121319),
    surface = Color(0xFF1C1D24),
    surfaceVariant = Color(0xFF2A2C36),
    onSurface = Color(0xFFE4E5EC),
    onSurfaceVariant = Color(0xFFC3C6D6)
)

@Composable
fun TodoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content
    )
}
