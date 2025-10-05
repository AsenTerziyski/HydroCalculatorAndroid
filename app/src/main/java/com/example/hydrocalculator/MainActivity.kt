package com.example.hydrocalculator

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
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
        hideSystemUI()
        setContent {
            HydrocalculatorTheme(darkTheme = true) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val isLoading by mainViewModel.startupState.collectAsState()

                    when (isLoading) {
                        LoadingAppUiState.LoadingSplashScreen -> {
                            Log.d("TAG101", "Showing splash screen")
                        }

                        LoadingAppUiState.LoadingWelcomeScreen -> {
                            WelcomeScreen()
                        }

                        LoadingAppUiState.Ready -> {
                            MainScreen()
                        }
                    }
                }
            }
        }
    }

    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.apply {
            hide(WindowInsetsCompat.Type.statusBars())
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}