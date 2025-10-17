package com.example.hydrocalculator.utils

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun UseTextAnimation(
    text: String,
    minFontSize: TextUnit,
    maxFontSize: TextUnit
): Pair<String, TextUnit> {
    var visibleText by remember { mutableStateOf("") }
    var startAnimation by remember { mutableStateOf(false) }

    val animatedFontSize by animateFloatAsState(
        targetValue = if (startAnimation) maxFontSize.value else minFontSize.value,
        label = "fontSizeAnimation",
        animationSpec = tween(durationMillis = if (text.isNotEmpty()) text.length * 100 else 0)
    )

    LaunchedEffect(text) {
        if (text.isNotEmpty()) {
            visibleText = ""
            startAnimation = false
            delay(100)
            startAnimation = true
            text.forEachIndexed { index, _ ->
                visibleText = text.substring(0, index + 1)
                delay(20)
            }
        } else {
            visibleText = ""
            startAnimation = false

        }
    }

    return Pair(visibleText, animatedFontSize.sp)
}