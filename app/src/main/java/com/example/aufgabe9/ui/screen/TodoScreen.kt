package com.example.aufgabe9.ui.screen


import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.aufgabe9.database.controller.TodoListController
import com.example.aufgabe9.database.dataclass.Priority
import com.example.aufgabe9.database.dataclass.TodoListDataClass


/**
 * Eine Compose-Funktion, die die Benutzeroberfläche für die ToDo-Ansicht darstellt.
 *
 * @param title Titel der aktuellen ToDo-Ansicht (z.B. "Offene Aufgaben").
 * @param filterCompleted Filterkriterium: Zeige nur abgeschlossene oder offene Aufgaben.
 * @param onNavigateToCompleted Callback, um zwischen offenen und erledigten Aufgaben zu wechseln.
 */
@Composable
fun TodoScreen(
    title: String,
    filterCompleted: Boolean,
    onNavigateToCompleted: () -> Unit
) {
    val context = LocalContext.current // Kontext für Toasts und andere kontextabhängige Operationen
    val todoListController = TodoListController(context) // Controller für ToDo-Datenbankoperationen

    // Zustand der gesamten ToDo-Liste und UI-Steuerung
    var todoList by remember { mutableStateOf(todoListController.getAllTodos()) } // Aktuelle Liste der ToDos
    var showEditDialog by remember { mutableStateOf(false) } // Steuert die Sichtbarkeit des Bearbeitungsdialogs
    var selectedTodo by remember { mutableStateOf<TodoListDataClass?>(null) } // Ausgewähltes ToDo zum Bearbeiten

    // Filtert die ToDos basierend auf dem Status
    val filteredTodos = todoList.filter { todo ->
        if (filterCompleted) todo.status == 1 else todo.status == 0
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    selectedTodo = null // Neues ToDo erstellen, kein ToDo vorausgewählt
                    showEditDialog = true // Dialog anzeigen
                },
                modifier = Modifier.padding(16.dp) // Abstand um den FAB herum
            ) {
                Text("+") // Text auf dem FAB
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) { // Inhalt der Seite
            // Header mit Titel und Navigation
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp), // Abstand nach unten
                horizontalArrangement = Arrangement.SpaceBetween, // Elemente links und rechts ausrichten
                verticalAlignment = Alignment.CenterVertically // Vertikale Zentrierung der Elemente
            ) {
                Text(
                    text = title, // Titel der Seite anzeigen
                    style = MaterialTheme.typography.titleLarge // Schriftstil anwenden
                )
                Button(onClick = onNavigateToCompleted) { // Button für Navigation zwischen Ansichten
                    Text(if (filterCompleted) "Zu Offenen" else "Zu Erledigten") // Beschriftung des Buttons
                }
            }

            // Anzeige der ToDo-Liste
            if (filteredTodos.isEmpty()) { // Wenn keine ToDos vorhanden sind
                Box(
                    modifier = Modifier.fillMaxSize(), // Box nimmt gesamten verfügbaren Platz ein
                    contentAlignment = Alignment.Center // Inhalt zentrieren
                ) {
                    Text("Keine ToDos gefunden.") // Nachricht anzeigen
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth() // Liste nimmt die gesamte Breite ein
                        .weight(1f), // Flexibles Layout
                    verticalArrangement = Arrangement.spacedBy(8.dp) // Abstand zwischen Elementen
                ) {
                    items(filteredTodos) { todo -> // Jedes ToDo wird als Karte dargestellt
                        TodoItemCard(
                            item = todo, // Daten des ToDos
                            /**
                             * Callback, der ausgeführt wird, wenn auf ein ToDo geklickt wird.
                             * Aktualisiert den Status des ToDos in der Datenbank.
                             *
                             * @param updatedTodo Das aktualisierte ToDo-Objekt.
                             */
                            onItemClicked = { updatedTodo ->
                                try {
                                    if (todoListController.updateTodo(updatedTodo)) {
                                        Toast.makeText(context, "Status aktualisiert", Toast.LENGTH_SHORT).show()
                                        todoList = todoListController.getAllTodos() // Liste aktualisieren
                                    } else {
                                        Toast.makeText(context, "Fehler beim Aktualisieren", Toast.LENGTH_SHORT).show()
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Fehler: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                            },
                            /**
                             * Callback, das ausgeführt wird, wenn ein ToDo gelöscht wird.
                             *
                             * @param todo Das zu löschende ToDo-Objekt.
                             */
                            onDeletClicked = {
                                try {
                                    if (todoListController.deleteTodoById(todo.id)) {
                                        Toast.makeText(context, "ToDo gelöscht", Toast.LENGTH_SHORT).show()
                                        todoList = todoListController.getAllTodos() // Liste aktualisieren
                                    } else {
                                        Toast.makeText(context, "Fehler beim Löschen", Toast.LENGTH_SHORT).show()
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Fehler: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                            },
                            /**
                             * Callback, das ausgeführt wird, wenn ein ToDo bearbeitet wird.
                             * Setzt das ausgewählte ToDo und zeigt den Bearbeitungsdialog an.
                             *
                             * @param todo Das zu bearbeitende ToDo-Objekt.
                             */
                            onEditClick = {
                                selectedTodo = todo // Ausgewähltes ToDo speichern
                                showEditDialog = true // Dialog anzeigen
                            }
                        )
                    }
                }
            }
        }
    }

    // Dialog für das Hinzufügen oder Bearbeiten eines ToDos
    if (showEditDialog) {
        EditTodoDialog(
            todo = selectedTodo, // Übergeben des ausgewählten ToDos (null für neues ToDo)
            /**
             * Callback, um den Bearbeitungsdialog zu schließen.
             */
            onDismiss = { showEditDialog = false }, // Schließen des Dialogs
            /**
             * Callback, um ein neues ToDo zu speichern oder ein bestehendes zu aktualisieren.
             *
             * @param todo Das zu speichernde oder zu aktualisierende ToDo-Objekt.
             */
            onSave = { todo ->
                try {
                    if (todo.id == 0) {
                        // Neues ToDo hinzufügen
                        if (todoListController.insertTodo(todo)) {
                            Toast.makeText(context, "ToDo hinzugefügt", Toast.LENGTH_SHORT).show()
                            todoList = todoListController.getAllTodos() // Liste aktualisieren
                        } else {
                            Toast.makeText(context, "Fehler beim Hinzufügen", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Bestehendes ToDo aktualisieren
                        if (todoListController.updateTodo(todo)) {
                            Toast.makeText(context, "ToDo aktualisiert", Toast.LENGTH_SHORT).show()
                            todoList = todoListController.getAllTodos() // Liste aktualisieren
                        } else {
                            Toast.makeText(context, "Fehler beim Aktualisieren", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Fehler: ${e.message}", Toast.LENGTH_SHORT).show()
                }
                showEditDialog = false // Dialog schließen
            },
            /**
             * Callback, um ein ToDo zu löschen.
             *
             * @param todo Das zu löschende ToDo-Objekt.
             */
            onDelete = { todo ->
                try {
                    if (todoListController.deleteTodoById(todo.id)) {
                        Toast.makeText(context, "ToDo gelöscht", Toast.LENGTH_SHORT).show()
                        todoList = todoListController.getAllTodos() // Liste aktualisieren
                    } else {
                        Toast.makeText(context, "Fehler beim Löschen", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Fehler: ${e.message}", Toast.LENGTH_SHORT).show()
                }
                showEditDialog = false // Dialog schließen
            }
        )
    }
}


/**
 * Composable function that represents a Todo item card in a list.
 *
 * This card displays the task name, priority, and a checkbox to mark the task as done.
 * It also provides options for editing (long click) and deleting the task.
 * Additionally, it has an expandable section for more task details like deadline and description.
 *
 * @param item The task item to display, encapsulated in a TodoListDataClass.
 *             It contains the task name, description, priority, deadline, and status.
 * @param onItemClicked Lambda function to handle the status change of the task
 *                      when the checkbox is toggled.
 * @param onDeletClicked Lambda function to handle the task deletion when the delete button is clicked.
 * @param onEditClick Lambda function to handle the editing of the task when the card is long-pressed.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TodoItemCard(
    item: TodoListDataClass,
    onItemClicked: (TodoListDataClass) -> Unit, // Updated ToDo when checkbox is clicked
    onDeletClicked: () -> Unit,
    onEditClick: () -> Unit
) {
    // State to manage the expanded visibility of the dropdown
    var expanded by remember { mutableStateOf(false) }


    // Card representing the todo item
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = { expanded = !expanded }, // Toggle the dropdown visibility
                onLongClick = onEditClick // Open edit dialog on long click
            ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            // Row for task name, priority, and action buttons (delete and expand)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Checkbox to mark the task as done
                Checkbox(
                    checked = item.status == 1,
                    onCheckedChange = { checked ->
                        onItemClicked(item.copy(status = if (checked) 1 else 0)) // Update task status
                    }
                )

                // Column displaying the task name and priority
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

                // Delete button for removing the task
                IconButton(onClick = { onDeletClicked() }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                }

                // Button to toggle the visibility of the dropdown (expand/collapse)
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (expanded) "Collapse" else "Expand"
                    )
                }
            }

            // Animated visibility for additional task details (visible when expanded)
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



/**
 * Composable function that displays a dialog for editing or adding a Todo item.
 *
 * This dialog allows users to add a new Todo or edit an existing one. It includes input fields
 * for the Todo's name, description, priority, and deadline. Additionally, if the Todo is being
 * edited, a delete option is available.
 *
 * @param todo The existing Todo item to edit, or `null` to add a new Todo. It contains the task's
 *             name, description, priority, deadline, and status.
 * @param onDismiss Lambda function to handle the dismissal of the dialog (typically called when
 *                  the user taps outside the dialog or cancels the action).
 * @param onSave Lambda function to save the edited or newly created Todo. It takes a `TodoListDataClass`
 *               instance with the updated information.
 * @param onDelete Optional lambda function to delete the Todo when the delete button is clicked.
 *                 This is only used when editing an existing Todo. If `null`, the delete button is not shown.
 */
@Composable
fun EditTodoDialog(
    todo: TodoListDataClass?,
    onDismiss: () -> Unit,
    onSave: (TodoListDataClass) -> Unit,
    onDelete: ((TodoListDataClass) -> Unit)? = null // Delete function, optional
) {
    // States to manage the values in the input fields
    var name by remember { mutableStateOf(todo?.name ?: "") }
    var description by remember { mutableStateOf(todo?.description ?: "") }
    var selectedPriority by remember { mutableStateOf(todo?.priority ?: Priority.MEDIUM) }
    var deadline by remember { mutableStateOf(todo?.deadline ?: "") }

    // AlertDialog to display the Todo edit form
    AlertDialog(
        onDismissRequest = onDismiss, // Handle dialog dismiss
        title = {
            Text(text = if (todo == null) "Add New ToDo" else "Edit ToDo")
        },
        text = {
            Column {
                // Input field for the Todo title
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Enter ToDo title") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Dropdown menu for priority selection
                var expanded by remember { mutableStateOf(false) }
                Text("Priority: ${selectedPriority.name}", modifier = Modifier.padding(bottom = 8.dp))
                IconButton(onClick = { expanded = true }) {
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    Priority.entries.forEach { priority ->
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

                // Input field for the Todo description
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Enter Description") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Input field for the Todo deadline
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
                        // Create a new Todo instance with the updated details
                        val updatedTodo = TodoListDataClass(
                            id = todo?.id ?: 0, // Use existing ID if editing or create a new one
                            name = name,
                            deadline = deadline,
                            description = description,
                            priority = selectedPriority,
                            status = todo?.status ?: 0 // Keep the same status if editing
                        )
                        onSave(updatedTodo) // Save the updated Todo
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            if (todo != null && onDelete != null) {
                // Delete button is available only when editing an existing Todo
                Button(
                    onClick = { onDelete(todo) } // Call the delete function
                ) {
                    Text("Delete")
                }
            }
        }
    )
}
