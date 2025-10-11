package com.example.hydrocalculator.views

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.sp
import com.example.hydrocalculator.ui.theme.hydroGradient
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HydroAppBar(title: String = "") {

    val fullTitle = title
    var visibleTitle by remember { mutableStateOf("") }
    var startAnimation by remember { mutableStateOf(false) }

    val minFontSize = 7.sp
    val maxFontSize = MaterialTheme.typography.titleLarge.fontSize

    val animatedFontSize by animateFloatAsState(
        targetValue = if (startAnimation) maxFontSize.value else minFontSize.value,
        label = "fontSizeAnimation",
        animationSpec = tween(durationMillis = fullTitle.length * 100)
    )

    LaunchedEffect(fullTitle) {
        if (fullTitle.isNotEmpty()) {
            visibleTitle = ""
            startAnimation = false
            delay(100)
            startAnimation = true
            fullTitle.forEachIndexed { index, _ ->
                visibleTitle = fullTitle.substring(0, index + 1)
                delay(20)
            }
        }
    }

    TopAppBar(
        title = {
            Text(
                text = visibleTitle,
                style = MaterialTheme.typography.titleLarge.copy(
                    brush = MaterialTheme.hydroGradient,
                    fontSize = animatedFontSize.sp
                )
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}