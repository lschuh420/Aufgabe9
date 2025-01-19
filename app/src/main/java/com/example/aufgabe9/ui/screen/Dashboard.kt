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
@Composable
fun Dashboard() {
    val navController = rememberNavController()
    var refreshTrigger by remember { mutableStateOf(0) }

    NavHost(navController = navController, startDestination = "todos") {
        composable("todos") {
            TodoScreen(
                title = "Todos",
                filterCompleted = false,
                onNavigateToCompleted = { navController.navigate("completed_todos") },

            )
        }
        composable("completed_todos") {
            TodoScreen(
                title = "Completed Todos",
                filterCompleted = true,
                onNavigateToCompleted = { navController.navigate("todos") },
            )
        }
    }
}