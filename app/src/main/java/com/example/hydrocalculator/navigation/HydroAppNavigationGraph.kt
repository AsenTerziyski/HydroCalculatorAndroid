package com.example.hydrocalculator.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hydrocalculator.AppScaffold
import com.example.hydrocalculator.views.ConfirmationDialog
import com.example.hydrocalculator.views.MainScreen
import com.example.hydrocalculator.views.WelcomeScreen

@Composable
fun HydroAppNavigationGraph() {
    val navController = rememberNavController()
    val activity = (LocalContext.current as? Activity)

    NavHost(
        navController = navController,
        startDestination = HydroAppRoutes.Welcome.route
    ) {
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

            var showDialog by remember { mutableStateOf(false) }

            // 3. Show the dialog when the state is true
            if (showDialog) {
                ConfirmationDialog(
                    dialogTitle = "Exit HydroCalculator?",
                    dialogText = "Are you sure you want to exit the app?",
                    onDismissRequest = {
                        // User clicked Cancel or outside the dialog
                        showDialog = false
                    },
                    onConfirmation = {
                        // User clicked Exit, so finish the activity
                        activity?.finish()
                    }
                )
            }


            AppScaffold(
                title = "Main Screen",
                onBackPressed = {
                    showDialog = true
                }
            ) { modifier ->
                MainScreen(modifier = modifier)
            }
        }
    }
}