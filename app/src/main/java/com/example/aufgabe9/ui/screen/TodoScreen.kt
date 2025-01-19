package com.example.aufgabe9.ui.screen

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box



import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.aufgabe9.database.dataclass.Priority
import com.example.aufgabe9.database.dataclass.TodoListDataClass

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aufgabe9.database.controller.TodoListController


@Composable
fun TodoScreen(
    title: String,
    filterCompleted: Boolean,
    onNavigateToCompleted: () -> Unit
) {
    val context = LocalContext.current
    val todoListController = TodoListController(context)

    // ToDo-Liste und Zustände
    var todoList by remember { mutableStateOf(todoListController.getAllTodos()) }
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedTodo by remember { mutableStateOf<TodoListDataClass?>(null) }

    // Gefilterte ToDos basierend auf Status
    val filteredTodos = todoList.filter { todo ->
        if (filterCompleted) todo.status == 1 else todo.status == 0
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    selectedTodo = null // Neues ToDo erstellen
                    showEditDialog = true
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("+")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // Header mit Titel und Navigation
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge
                )
                Button(onClick = onNavigateToCompleted) {
                    Text(if (filterCompleted) "Zu Offenen" else "Zu Erledigten")
                }
            }

            // ToDo-Liste anzeigen
            if (filteredTodos.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Keine ToDos gefunden.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredTodos) { todo ->
                        TodoItemCard(
                            item = todo,
                            onItemClicked = { updatedTodo ->try {
                                if (todoListController.updateTodo(updatedTodo)) {
                                    Toast.makeText(context, "Status aktualisiert", Toast.LENGTH_SHORT).show()
                                    todoList = todoListController.getAllTodos()
                                } else {
                                    Toast.makeText(context, "Fehler beim Aktualisieren", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Fehler: ${e.message}", Toast.LENGTH_SHORT).show()
                            }

                            },
                            onDeletClicked = {
                                try {
                                    if (todoListController.deleteTodoById(todo.id)) {
                                        Toast.makeText(context, "ToDo gelöscht", Toast.LENGTH_SHORT).show()
                                        todoList = todoListController.getAllTodos()                                    } else {
                                        Toast.makeText(context, "Fehler beim Löschen", Toast.LENGTH_SHORT).show()
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Fehler: ${e.message}", Toast.LENGTH_SHORT).show()
                                }

                            },
                            onEditClick = {
                                selectedTodo = todo
                                showEditDialog = true
                            }
                        )
                    }
                }
            }
        }
    }

    // Bearbeitungs-Dialog
    if (showEditDialog) {
        EditTodoDialog(
            todo = selectedTodo,
            onDismiss = { showEditDialog = false },
            onSave = { todo ->
                try {
                    if (todo.id == 0) {
                        // Neues ToDo hinzufügen
                        if (todoListController.insertTodo(todo)) {
                            Toast.makeText(context, "ToDo hinzugefügt", Toast.LENGTH_SHORT).show()
                            todoList = todoListController.getAllTodos()
                        } else {
                            Toast.makeText(context, "Fehler beim Hinzufügen", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Bestehendes ToDo aktualisieren
                        if (todoListController.updateTodo(todo)) {
                            Toast.makeText(context, "ToDo aktualisiert", Toast.LENGTH_SHORT).show()
                            todoList = todoListController.getAllTodos()
                        } else {
                            Toast.makeText(context, "Fehler beim Aktualisieren", Toast.LENGTH_SHORT).show()
                        }
                    }
                }catch (e: Exception) {
                    Toast.makeText(context, "Fehler: ${e.message}", Toast.LENGTH_SHORT).show()
                }
                showEditDialog = false
            },
            onDelete = { todo ->
                try {
                    if (todoListController.deleteTodoById(todo.id)) {
                        Toast.makeText(context, "ToDo gelöscht", Toast.LENGTH_SHORT).show()
                        todoList = todoListController.getAllTodos()
                    } else {
                        Toast.makeText(context, "Fehler beim Löschen", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Fehler: ${e.message}", Toast.LENGTH_SHORT).show()
                }
                showEditDialog = false
            }
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TodoItemCard(
    item: TodoListDataClass,
    onItemClicked: (TodoListDataClass) -> Unit, // Updated ToDo when checkbox is clicked
    onDeletClicked: () -> Unit,
    onEditClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) } // For dropdown expansion
    var isChecked by remember { mutableStateOf(item.status == 1) } // Local checkbox state

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { expanded = !expanded }, // Toggles the dropdown
                onLongClick = onEditClick // Opens edit dialog
            ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Checkbox to toggle ToDo status
                Checkbox(
                    checked = item.status == 1,
                    onCheckedChange = { checked ->
                        onItemClicked(item.copy(status = if (checked) 1 else 0))
                    }
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Priority: ${item.priority.name}",
                        style = MaterialTheme.typography.bodySmall,
                        color = when (item.priority) {
                            Priority.HIGH -> MaterialTheme.colorScheme.error
                            Priority.MEDIUM -> MaterialTheme.colorScheme.primary
                            Priority.LOW -> MaterialTheme.colorScheme.onSurface
                        }
                    )
                }
                // Delete icon
                IconButton(onClick = { onDeletClicked() }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                }
                // Dropdown toggle icon
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (expanded) "Collapse" else "Expand"
                    )
                }
            }

            // Expanded content for additional details
            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text("Deadline: ${item.deadline}", style = MaterialTheme.typography.bodyMedium)
                    Text("Description: ${item.description}", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Long press to edit",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }
        }
    }
}



@Composable
fun EditTodoDialog(
    todo: TodoListDataClass?,
    onDismiss: () -> Unit,
    onSave: (TodoListDataClass) -> Unit,
    onDelete: ((TodoListDataClass) -> Unit)? = null // Delete function, optional
) {
    var name by remember { mutableStateOf(todo?.name ?: "") }
    var description by remember { mutableStateOf(todo?.description ?: "") }
    var selectedPriority by remember { mutableStateOf(todo?.priority ?: Priority.MEDIUM) }
    var deadline by remember { mutableStateOf(todo?.deadline ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = if (todo == null) "Add New ToDo" else "Edit ToDo")
        },
        text = {
            Column {
                // Input field for title
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Enter ToDo title") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Dropdown for priority
                var expanded by remember { mutableStateOf(false) }
                Text("Priority: ${selectedPriority.name}", modifier = Modifier.padding(bottom = 8.dp))
                IconButton(onClick = { expanded = true }) {
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    Priority.values().forEach { priority ->
                        DropdownMenuItem(
                            text = { Text(priority.name) },
                            onClick = {
                                selectedPriority = priority
                                expanded = false
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Input field for description
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Enter Description") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Input field for deadline
                OutlinedTextField(
                    value = deadline,
                    onValueChange = { deadline = it },
                    label = { Text("Enter Deadline") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        val updatedTodo = TodoListDataClass(
                            id = todo?.id ?: 0, // Use existing ID or create a new one
                            name = name,
                            deadline = deadline,
                            description = description,
                            priority = selectedPriority,
                            status = todo?.status ?: 0 // Keep the same status if editing
                        )
                        onSave(updatedTodo) // Save or update the ToDo
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            if (todo != null && onDelete != null) {
                Button(
                    onClick = { onDelete(todo) } // Call delete function
                ) {
                    Text("Delete")
                }
            }
        }
    )
}
