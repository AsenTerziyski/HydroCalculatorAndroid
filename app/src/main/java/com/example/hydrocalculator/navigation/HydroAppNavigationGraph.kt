package com.example.hydrocalculator.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hydrocalculator.AppScaffold
import com.example.hydrocalculator.views.MainScreen
import com.example.hydrocalculator.views.WelcomeScreen

@Composable
fun HydroAppNavigationGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = HydroAppRoutes.Welcome.route
    ) {
        // Welcome Screen
        composable(route = HydroAppRoutes.Welcome.route) {
            WelcomeScreen(
                onWelcomeComplete = {
                    navController.navigate(route = HydroAppRoutes.Main.route) {
                        popUpTo(HydroAppRoutes.Welcome.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = HydroAppRoutes.Main.route) {
            AppScaffold(
                title = "Main Screen"
            ) { modifier ->
                MainScreen(modifier = modifier)
            }
        }
    }
}