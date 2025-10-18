package com.example.hydrocalculator.navigation

import android.app.Activity
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.width
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hydrocalculator.AppScaffold
import com.example.hydrocalculator.R
import com.example.hydrocalculator.views.ConfirmationDialog
import com.example.hydrocalculator.views.GoodbyeScreen
import com.example.hydrocalculator.views.MainScreen
import com.example.hydrocalculator.views.WelcomeScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HydroAppNavigationGraph() {
    val navController = rememberNavController()
    val activity = (LocalContext.current as? Activity)

    val animationSpec = tween<IntOffset>(700) // Define animation spec once
    NavHost(
        navController = navController,
        startDestination = HydroAppRoutes.Welcome.route,
        enterTransition = {
            slideIn(
                initialOffset = { fullSize -> IntOffset(fullSize.width, 0) },
                animationSpec = animationSpec
            )
        },
        exitTransition = {
            slideOut(
                targetOffset = { fullSize -> IntOffset(-fullSize.width, 0) },
                animationSpec = animationSpec
            )
        },
        popEnterTransition = {
            slideIn(
                initialOffset = { fullSize -> IntOffset(-fullSize.width, 0) },
                animationSpec = animationSpec
            )
        },
        popExitTransition = {
            slideOut(
                targetOffset = { fullSize -> IntOffset(fullSize.width, 0) },
                animationSpec = animationSpec
            )
        }
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

            if (showDialog) {
                ConfirmationDialog(
                    dialogTitle = stringResource(R.string.exit_hydrocalculator),
                    dialogText = stringResource(R.string.are_you_sure_you_want_to_exit_the_app),
                    onDismissRequest = {
                        showDialog = false
                    },
                    onConfirmation = {
                        showDialog = false
                        navController.navigate(route = HydroAppRoutes.Goodbye.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            AppScaffold(
                title = "Main Screen",
                onBackPressed = { showDialog = true }
            ) { modifier ->
                MainScreen(modifier = modifier)
            }
        }

        composable(
            route = HydroAppRoutes.Goodbye.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            GoodbyeScreen { activity?.finish() }
        }
    }
}