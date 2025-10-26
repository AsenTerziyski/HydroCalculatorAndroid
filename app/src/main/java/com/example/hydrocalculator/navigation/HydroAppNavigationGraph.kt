package com.example.hydrocalculator.navigation

import android.app.Activity
import android.util.Log
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hydrocalculator.AppScaffold
import com.example.hydrocalculator.R
import com.example.hydrocalculator.views.ConfirmationDialog
import com.example.hydrocalculator.views.hydroappbars.HydroAppBottomBar
import com.example.hydrocalculator.views.screens.CalculationPressureScreen
import com.example.hydrocalculator.views.screens.CalculationTypeScreen
import com.example.hydrocalculator.views.screens.GoodbyeScreen
import com.example.hydrocalculator.views.screens.WelcomeScreen

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
                    navController.navigate(route = HydroAppRoutes.CalculationType.route) {
                        popUpTo(HydroAppRoutes.Welcome.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = HydroAppRoutes.CalculationType.route) {
            var showDialog by remember { mutableStateOf(false) }
            var topBarTitle by remember { mutableStateOf("") }
            if (showDialog) {
                ConfirmationDialog(
                    dialogTitle = stringResource(R.string.exit_hydrocalculator),
                    dialogText = stringResource(R.string.are_you_sure_you_want_to_exit_the_app),
                    onDismissRequest = { showDialog = false },
                    onConfirmation = {
                        showDialog = false
                        navController.navigate(route = HydroAppRoutes.Goodbye.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            AppScaffold(
                title = topBarTitle,
                icon = null,
                bottomBar = {
                    HydroAppBottomBar(
                        onSwitchOfClick = {
                            showDialog = true
                        }
                    )
                },
                onBackPressed = null
            ) { modifier ->
                CalculationNavGraph(
                    navController = rememberNavController(),
                    onScreenChange = { newTitle ->
                        topBarTitle = newTitle
                    }
                )
            }
        }

        composable(
            route = HydroAppRoutes.Goodbye.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            GoodbyeScreen { activity?.finish() }
        }
    }
}

@Composable
private fun CalculationNavGraph(
    navController: NavHostController,
    onScreenChange: (String) -> Unit
) {
    val animationSpec = tween<IntOffset>(700)
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = HydroAppRoutes.CalculationType.route,
        enterTransition = {
            slideIn(
                initialOffset = { IntOffset(it.width, 0) },
                animationSpec = animationSpec
            )
        },
        exitTransition = {
            slideOut(
                targetOffset = { IntOffset(-it.width, 0) },
                animationSpec = animationSpec
            )
        },
        popEnterTransition = {
            slideIn(
                initialOffset = { IntOffset(-it.width, 0) },
                animationSpec = animationSpec
            )
        },
        popExitTransition = {
            slideOut(
                targetOffset = { IntOffset(it.width, 0) },
                animationSpec = animationSpec
            )
        }
    ) {
        composable(route = HydroAppRoutes.CalculationType.route) {

            LaunchedEffect(Unit) {
                onScreenChange.invoke(context.getString(R.string.calculation_type))
            }

            CalculationTypeScreen { calcType ->
                when (calcType.title) {
                    "Pressurized Pipes" -> {
                        navController.navigate(HydroAppRoutes.PressureScreen.route)
                    }

                    "Gravity Pipes" -> {
                        Log.d("Navigation", "Navigate to Gravity Pipes screen")
                    }

                    "Your results" -> {
                        Log.d("Navigation", "Navigate to Results screen")
                    }
                }
            }
        }

        composable(route = HydroAppRoutes.PressureScreen.route) {
            LaunchedEffect(Unit) {
                onScreenChange.invoke(context.getString(R.string.pressurized_pipes))
            }
            CalculationPressureScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
