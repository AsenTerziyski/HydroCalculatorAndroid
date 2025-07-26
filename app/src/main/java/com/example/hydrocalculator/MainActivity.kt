package com.example.hydrocalculator

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hydrocalculator.ui.theme.HydrocalculatorTheme
import com.example.hydrocalculator.view.HydroCalculatorScreen
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HydrocalculatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HydroCalculatorAppEntry(
                        onAppReady = {
                            enableEdgeToEdge()
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun HydroCalculatorAppEntry(
    onAppReady: () -> Unit
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SystemBarsController(showSystemBars = false) // Hide system bars
            SplashScreen()
            LaunchedEffect(Unit) {
                delay(3000)
                onAppReady.invoke()
                navController.navigate("hydroCalculator") {
                    popUpTo("splash") { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
        composable("hydroCalculator") {
            // Ensure system bars are shown for the main content screen
            // And call onAppReady here to enable edge-to-edge for the main app.
            SystemBarsController(showSystemBars = true, onSystemBarsReady = onAppReady)
            HydroCalculatorScreen()
        }
    }
}

@Composable
fun SystemBarsController(showSystemBars: Boolean, onSystemBarsReady: (() -> Unit)? = null) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        DisposableEffect(Unit, showSystemBars) {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                val insetsController = WindowCompat.getInsetsController(window, view)
                if (showSystemBars) {
                    insetsController.show(WindowInsetsCompat.Type.systemBars())
                    onSystemBarsReady?.invoke()
                } else {
                    // Hide both status and navigation bars
                    insetsController.hide(WindowInsetsCompat.Type.systemBars()) // This hides both
                    // Or individually:
                    // insetsController.hide(WindowInsetsCompat.Type.statusBars())
                    // insetsController.hide(WindowInsetsCompat.Type.navigationBars())

                    insetsController.systemBarsBehavior =
                        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                }
            }
            onDispose {
                // Typically, the next screen's SystemBarsController will handle showing them.
                // If you wanted to be absolutely sure they reappear if this composable is
                // disposed for reasons other than navigation to a screen that also controls them:
                // val window = (view.context as? Activity)?.window
                // if (window != null) {
                // WindowCompat.getInsetsController(window, view).show(WindowInsetsCompat.Type.systemBars())
                // }
            }
        }
    }
}
