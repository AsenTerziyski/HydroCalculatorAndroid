package com.example.hydrocalculator.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import com.example.hydrocalculator.utils.UseTextAnimation
import kotlinx.coroutines.delay

@Composable
fun GoodbyeScreen(onGoodbyeComplete: () -> Unit) {

    LaunchedEffect(Unit) {
        delay(1000)
        onGoodbyeComplete.invoke()
    }
    val title = stringResource(R.string.goodbye_hydro)

    val (visibleTitle, animatedFontSize) = UseTextAnimation(
        text = title,
        minFontSize = 42.sp,
        maxFontSize = 0.sp
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = visibleTitle,
                style = TextStyle(
                    brush = MaterialTheme.hydroGradient,
                    fontSize = animatedFontSize,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}