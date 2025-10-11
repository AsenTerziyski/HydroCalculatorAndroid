package com.example.hydrocalculator

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hydrocalculator.navigation.HydroAppRoutes
import com.example.hydrocalculator.ui.theme.HydrocalculatorTheme
import com.example.hydrocalculator.views.MainScreen
import com.example.hydrocalculator.views.WelcomeScreen
import com.example.hydrocalculator.vm.LoadingAppUiState
import com.example.hydrocalculator.vm.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition {
            mainViewModel.startupState.value == LoadingAppUiState.LoadingSplashScreen
        }
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            HydrocalculatorTheme(
                darkTheme = true,
                dynamicColor = false
            ) {

                val navController = rememberNavController()
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
                        AppScaffold(
                            title = "Main Screen"
                        ) { modifier -> MainScreen(modifier = modifier) }
                    }
                }
            }
        }
    }
}

@Composable
fun AppScaffold(title: String, content: @Composable (Modifier) -> Unit) {
    Scaffold(
        topBar = { HydroAppBar(title) }
    ) { innerPadding ->
        content(Modifier.padding(innerPadding))
    }
}