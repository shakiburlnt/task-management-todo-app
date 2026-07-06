package com.devin.todo.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.devin.todo.data.nowMillis
import com.devin.todo.model.Priority
import com.devin.todo.model.Task

/**
 * Full-screen editor rendered inline in the main composition, giving a focused
 * mobile-style editing surface on both Android and Web.
 */
@Composable
fun TaskEditorDialog(
    existing: Task?,
    onDismiss: () -> Unit,
    onSave: (title: String, notes: String, priority: Priority, dueDate: Long?) -> Unit
) {
    var title by remember { mutableStateOf(existing?.title ?: "") }
    var notes by remember { mutableStateOf(existing?.notes ?: "") }
    var priority by remember { mutableStateOf(existing?.priority ?: Priority.MEDIUM) }
    var dueDate by remember { mutableStateOf(existing?.dueDate) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
            Column(
                modifier = Modifier
                    .widthIn(max = 560.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(Modifier.height(24.dp))
                Text(
                    if (existing == null) "New task" else "Edit task",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(20.dp))
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    placeholder = { Text("Notes") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(20.dp))
                Text("Priority", fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Priority.entries.forEach { p ->
                        FilterChip(
                            selected = priority == p,
                            onClick = { priority = p },
                            label = { Text(p.label) },
                            colors = FilterChipDefaults.filterChipColors()
                        )
                    }
                }
                Spacer(Modifier.height(20.dp))
                Text("Due date", fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val day = 24L * 60 * 60 * 1000
                    DueChip("None", dueDate == null) { dueDate = null }
                    DueChip("Today", isSameDay(dueDate, nowMillis())) { dueDate = nowMillis() }
                    DueChip("Tomorrow", isSameDay(dueDate, nowMillis() + day)) {
                        dueDate = nowMillis() + day
                    }
                    DueChip("+1 week", isSameDay(dueDate, nowMillis() + 7 * day)) {
                        dueDate = nowMillis() + 7 * day
                    }
                }
                if (dueDate != null) {
                    Spacer(Modifier.height(8.dp))
                    Text("Due: ${formatDueDate(dueDate!!)}")
                }
                Spacer(Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) { Text("Cancel") }
                    Spacer(Modifier.height(0.dp))
                    TextButton(
                        onClick = { onSave(title, notes, priority, dueDate) },
                        enabled = title.isNotBlank()
                    ) { Text("Save") }
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun DueChip(label: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label) }
    )
}

private fun isSameDay(a: Long?, b: Long): Boolean {
    if (a == null) return false
    val day = 24L * 60 * 60 * 1000
    return a / day == b / day
}
