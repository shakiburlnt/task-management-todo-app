package com.devin.todo.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.devin.todo.data.nowMillis
import com.devin.todo.model.Priority
import com.devin.todo.model.Task

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

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = { onSave(title, notes, priority, dueDate) },
                enabled = title.isNotBlank()
            ) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        title = { Text(if (existing == null) "New task" else "Edit task") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(16.dp))
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
                Spacer(Modifier.height(16.dp))
                Text("Due date", fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
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
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
private fun DueChip(label: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(label) }
    )
    Spacer(Modifier.width(0.dp))
}

private fun isSameDay(a: Long?, b: Long): Boolean {
    if (a == null) return false
    val day = 24L * 60 * 60 * 1000
    return a / day == b / day
}
