package com.example.aufgabe9.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
