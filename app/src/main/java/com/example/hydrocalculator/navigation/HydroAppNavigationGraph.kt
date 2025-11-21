package com.example.hydrocalculator.navigation

import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.hydrocalculator.R
import com.example.hydrocalculator.ui.views.dialogs.ConfirmationDialog
import com.example.hydrocalculator.ui.views.hydroappbars.HydroAppBottomBar
import com.example.hydrocalculator.ui.views.hydroappbars.HydroAppTopBar
import com.example.hydrocalculator.ui.views.screens.CalculationPressureScreen
import com.example.hydrocalculator.ui.views.screens.CalculationTypeScreen
import com.example.hydrocalculator.ui.views.screens.GoodbyeScreen
import com.example.hydrocalculator.ui.views.screens.WelcomeScreen

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
                navController.navigate(route = HydroAppRoutes.GoodbyeScreen.route) {
                    popUpTo(0) { inclusive = true }
                }
            })
    }

    HydroAppScaffold(
        topBar = {
            val topBarTitle = when (currentRoute) {
                HydroAppRoutes.HomeScreen.route -> stringResource(R.string.calculation_type)
                HydroAppRoutes.PressureScreen.route -> stringResource(R.string.pressurized_pipes)
                else -> ""
            }
            HydroAppTopBar(
                title = topBarTitle,
                icon = if (currentRoute == HydroAppRoutes.PressureScreen.route) Icons.Default.ArrowBack else null
            ) { navController.popBackStack() }
        },
        bottomBar = {
            val selectedTab = when (currentRoute) {
                HydroAppRoutes.PressureScreen.route -> BottomBarTab.PRESSURIZED_PIPES
                HydroAppRoutes.HomeScreen.route -> BottomBarTab.HOME
                else -> null
            }
            if (currentRoute == HydroAppRoutes.HomeScreen.route || currentRoute == HydroAppRoutes.PressureScreen.route) {
                HydroAppBottomBar(
                    currentlySelectedTab = selectedTab,
                    onClickHome = {
                        navController.navigate(route = HydroAppRoutes.HomeScreen.route)
                    },
                    onClickPressurizedPipes = {
                        if (currentRoute != HydroAppRoutes.PressureScreen.route) {
                            navController.navigate(route = HydroAppRoutes.PressureScreen.route)
                        }
                    },
                    onSwitchOfClick = { showDialog = true })
            }
        },
    ) { scaffoldModifier ->

        val animationSpec = tween<IntOffset>(700)
        NavHost(
            navController = navController,
            modifier = scaffoldModifier,
            startDestination = HydroAppRoutes.WelcomeScreen.route,
            enterTransition = {
                slideIn(
                    initialOffset = { IntOffset(it.width, 0) }, animationSpec = animationSpec
                )
            },
            exitTransition = {
                slideOut(
                    targetOffset = { IntOffset(-it.width, 0) }, animationSpec = animationSpec
                )
            },
            popEnterTransition = {
                slideIn(
                    initialOffset = { IntOffset(-it.width, 0) }, animationSpec = animationSpec
                )
            },
            popExitTransition = {
                slideOut(
                    targetOffset = { IntOffset(it.width, 0) }, animationSpec = animationSpec
                )
            }) {

            composable(route = HydroAppRoutes.WelcomeScreen.route) {
                WelcomeScreen(
                    onWelcomeComplete = {
                        navController.navigate(route = HydroAppRoutes.HomeScreen.route) {
                            popUpTo(HydroAppRoutes.WelcomeScreen.route) { inclusive = true }
                        }
                    })
            }

            composable(route = HydroAppRoutes.GoodbyeScreen.route) {
                GoodbyeScreen { activity?.finish() }
            }

            composable(route = HydroAppRoutes.HomeScreen.route) {
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
                CalculationPressureScreen()
            }
        }
    }
}

@Composable
fun HydroAppScaffold(
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (Modifier) -> Unit
) {
    Scaffold(
        topBar = topBar,
        bottomBar = bottomBar,
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        content(
            Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        )
    }
}
