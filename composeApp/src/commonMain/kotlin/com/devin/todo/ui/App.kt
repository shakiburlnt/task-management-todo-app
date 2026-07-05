package com.devin.todo.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.devin.todo.model.Task

@Composable
fun App(viewModel: TaskViewModel) {
    TodoTheme {
        val state by viewModel.uiState.collectAsState()
        var editorTask by remember { mutableStateOf<Task?>(null) }
        var showEditor by remember { mutableStateOf(false) }
        var creating by remember { mutableStateOf(false) }
        var sortMenuOpen by remember { mutableStateOf(false) }

        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            contentWindowInsets = WindowInsets.safeDrawing,
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = {
                        editorTask = null
                        creating = true
                        showEditor = true
                    },
                    icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                    text = { Text("New task") }
                )
            }
        ) { padding ->
            Box(Modifier.fillMaxSize().padding(padding)) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 720.dp)
                        .align(Alignment.TopCenter)
                        .padding(horizontal = 16.dp)
                ) {
                    Spacer(Modifier.height(12.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(
                                "Tasks",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "${state.activeCount} active \u00B7 ${state.completedCount} done",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Box {
                            IconButton(onClick = { sortMenuOpen = true }) {
                                Icon(Icons.AutoMirrored.Filled.Sort, contentDescription = "Sort")
                            }
                            DropdownMenu(
                                expanded = sortMenuOpen,
                                onDismissRequest = { sortMenuOpen = false }
                            ) {
                                TaskSort.entries.forEach { s ->
                                    DropdownMenuItem(
                                        text = { Text(s.label) },
                                        onClick = {
                                            viewModel.setSort(s)
                                            sortMenuOpen = false
                                        },
                                        trailingIcon = {
                                            if (state.sort == s) {
                                                Text("\u2713", color = MaterialTheme.colorScheme.primary)
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = state.query,
                        onValueChange = { viewModel.setQuery(it) },
                        placeholder = { Text("Search tasks") },
                        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(12.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())
                    ) {
                        TaskFilter.entries.forEach { f ->
                            FilterChip(
                                selected = state.filter == f,
                                onClick = { viewModel.setFilter(f) },
                                label = { Text(f.label) }
                            )
                        }
                        Spacer(Modifier.weight(1f))
                        if (state.completedCount > 0) {
                            TextButton(onClick = { viewModel.clearCompleted() }) {
                                Text("Clear done")
                            }
                        }
                    }

                    Spacer(Modifier.height(8.dp))
                    if (state.visibleTasks.isEmpty()) {
                        EmptyState(state.query.isNotBlank())
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 2.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(state.visibleTasks, key = { it.id }) { task ->
                                TaskItem(
                                    task = task,
                                    onToggle = { viewModel.toggleComplete(task.id) },
                                    onClick = {
                                        editorTask = task
                                        creating = false
                                        showEditor = true
                                    },
                                    onDelete = { viewModel.deleteTask(task.id) }
                                )
                            }
                        }
                    }
                }
            }

            if (showEditor) {
                TaskEditorDialog(
                    existing = if (creating) null else editorTask,
                    onDismiss = { showEditor = false },
                    onSave = { title, notes, priority, dueDate ->
                        val current = editorTask
                        if (creating || current == null) {
                            viewModel.addTask(title, notes, priority, dueDate)
                        } else {
                            viewModel.updateTask(
                                current.copy(
                                    title = title.trim(),
                                    notes = notes.trim(),
                                    priority = priority,
                                    dueDate = dueDate
                                )
                            )
                        }
                        showEditor = false
                    }
                )
            }
        }
    }
}

@Composable
private fun EmptyState(fromSearch: Boolean) {
    Box(Modifier.fillMaxWidth().height(240.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                if (fromSearch) "No matching tasks" else "Nothing here yet",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                if (fromSearch) "Try a different search." else "Tap \u201CNew task\u201D to add one.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
