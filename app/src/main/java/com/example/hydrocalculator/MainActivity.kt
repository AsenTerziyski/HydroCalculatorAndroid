package com.example.hydrocalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.example.hydrocalculator.navigation.HydroAppNavigationGraph
import com.example.hydrocalculator.ui.theme.HydrocalculatorTheme
import com.example.hydrocalculator.views.hydroappbars.HydroAppTopBar
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
            ) { HydroAppNavigationGraph() }
        }
    }
}

@Composable
fun AppScaffold(
    title: String,
    icon: ImageVector? = null,
    onBackPressed: (() -> Unit)? = null,
    content: @Composable (Modifier) -> Unit
) {
    Scaffold(
        topBar = {
            HydroAppTopBar(title = title, icon = icon) { onBackPressed?.invoke() }
        }
    ) { innerPadding ->
        content(Modifier.padding(innerPadding))
    }
}