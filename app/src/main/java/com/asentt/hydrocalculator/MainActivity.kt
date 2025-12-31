package com.asentt.hydrocalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.asentt.hydrocalculator.navigation.HydroAppNavigationGraph
import com.asentt.hydrocalculator.ui.theme.HydroCalculatorTheme
import com.asentt.hydrocalculator.vm.LoadingAppUiState
import com.asentt.hydrocalculator.vm.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition {
            mainViewModel.startupState.value == LoadingAppUiState.LoadingSplashScreen
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HydroCalculatorTheme(
                darkTheme = true,
                dynamicColor = false
            ) { HydroAppNavigationGraph(mainViewModel) }
        }
    }
}
