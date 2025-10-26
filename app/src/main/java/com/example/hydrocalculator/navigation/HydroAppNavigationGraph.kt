package com.example.hydrocalculator.navigation

import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.hydrocalculator.R
import com.example.hydrocalculator.views.ConfirmationDialog
import com.example.hydrocalculator.views.hydroappbars.HydroAppBottomBar
import com.example.hydrocalculator.views.hydroappbars.HydroAppTopBar
import com.example.hydrocalculator.views.screens.CalculationPressureScreen
import com.example.hydrocalculator.views.screens.CalculationTypeScreen
import com.example.hydrocalculator.views.screens.GoodbyeScreen
import com.example.hydrocalculator.views.screens.WelcomeScreen

@Composable
fun HydroAppNavigationGraph() {
    val navController = rememberNavController()
    val activity = (LocalContext.current as? Activity)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    var showDialog by remember { mutableStateOf(false) }
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

    HydroAppScaffold(
        title = when (currentRoute) {
            HydroAppRoutes.CalculationType.route -> stringResource(R.string.calculation_type)
            HydroAppRoutes.PressureScreen.route -> stringResource(R.string.pressurized_pipes)
            else -> ""
        },
        icon = if (currentRoute == HydroAppRoutes.PressureScreen.route) Icons.Default.ArrowBack else null,
        bottomBar = {
            if (currentRoute == HydroAppRoutes.CalculationType.route || currentRoute == HydroAppRoutes.PressureScreen.route) {
                HydroAppBottomBar(onSwitchOfClick = { showDialog = true })
            }
        },
        onBackPressed = {
            navController.popBackStack()
        }
    ) {
        val animationSpec = tween<IntOffset>(700)
        NavHost(
            navController = navController,
            startDestination = HydroAppRoutes.Welcome.route,
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
            composable(route = HydroAppRoutes.Welcome.route) {
                WelcomeScreen(
                    onWelcomeComplete = {
                        navController.navigate(route = HydroAppRoutes.CalculationType.route) {
                            popUpTo(HydroAppRoutes.Welcome.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(route = HydroAppRoutes.Goodbye.route) {
                GoodbyeScreen { activity?.finish() }
            }

            composable(route = HydroAppRoutes.CalculationType.route) {
                BackHandler { showDialog = true }
                CalculationTypeScreen { calcType ->
                    when (calcType.title) {
                        "Pressurized Pipes" -> navController.navigate(HydroAppRoutes.PressureScreen.route)
                        "Gravity Pipes" -> Log.d("Navigation", "Navigate to Gravity Pipes screen")
                        "Your results" -> Log.d("Navigation", "Navigate to Results screen")
                    }
                }
            }

            composable(route = HydroAppRoutes.PressureScreen.route) {
                CalculationPressureScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}

@Composable
fun HydroAppScaffold(
    title: String,
    icon: ImageVector? = null,
    onBackPressed: (() -> Unit)? = null,
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (Modifier) -> Unit
) {
    Scaffold(
        topBar = {
            HydroAppTopBar(title = title, icon = icon) { onBackPressed?.invoke() }
        },
        bottomBar = bottomBar
    ) { innerPadding ->
        content(Modifier.padding(innerPadding))
    }
}
