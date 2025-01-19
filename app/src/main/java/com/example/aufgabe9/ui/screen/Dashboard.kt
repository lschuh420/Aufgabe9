package com.example.aufgabe9.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
/**
 * Composable function representing the main Dashboard screen of the app.
 *
 * This composable manages the navigation between the "Todos" and "Completed Todos" screens.
 * It uses a `NavController` to handle the navigation between different destinations.
 * The screen is responsible for controlling the navigation logic and refreshing the content.
 *
 * @Composable
 * @see NavController
 * @see TodoScreen
 */
@Composable
fun Dashboard() {
    // Initialize NavController for handling navigation
    val navController = rememberNavController()

    // State for triggering content refresh (e.g., after adding a new ToDo)
    var refreshTrigger by remember { mutableStateOf(0) }

    // Define the navigation graph for the app
    NavHost(navController = navController, startDestination = "todos") {
        // Composable for the "Todos" screen
        composable("todos") {
            TodoScreen(
                title = "Todos", // Screen title
                filterCompleted = false, // Show active todos
                onNavigateToCompleted = { navController.navigate("completed_todos") }, // Navigate to completed todos screen
            )
        }

        // Composable for the "Completed Todos" screen
        composable("completed_todos") {
            TodoScreen(
                title = "Completed Todos", // Screen title
                filterCompleted = true, // Show completed todos
                onNavigateToCompleted = { navController.navigate("todos") }, // Navigate back to the todos screen
            )
        }
    }
}
