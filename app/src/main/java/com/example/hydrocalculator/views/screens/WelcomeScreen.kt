package com.example.hydrocalculator.views.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.hydrocalculator.R
import com.example.hydrocalculator.ui.theme.hydroGradient
import com.example.hydrocalculator.views.FullScreenEffect
import com.example.hydrocalculator.views.LoadingAppView
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(onWelcomeComplete: () -> Unit = {}) {
    FullScreenEffect()

    LaunchedEffect(Unit) {
        delay(3000)
        onWelcomeComplete.invoke()
    }
    Surface(
        modifier = Modifier.fillMaxSize().statusBarsPadding().navigationBarsPadding(),
        color = MaterialTheme.colorScheme.background //
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.welcome_hydro),
                style = TextStyle(
                    brush = MaterialTheme.hydroGradient,
                    fontSize = 37.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            )
            LoadingAppView()
        }
    }
}