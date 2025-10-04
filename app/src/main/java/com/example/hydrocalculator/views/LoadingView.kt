package com.example.hydrocalculator.views

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import kotlin.text.append

@Composable
fun LoadingView(
    modifier: Modifier = Modifier,
    text: String = "Loading",
    color: Color = Color.White
) {
    var visibleDotCount by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            visibleDotCount = (visibleDotCount + 1) % 4
            delay(700)
        }
    }

    val annotatedString = buildAnnotatedString {
        append(text)
        for (i in 1..3) {
            val dotColor = if (i <= visibleDotCount) color else Color.Transparent
            withStyle(style = SpanStyle(color = dotColor)) {
                append(".")
            }
        }
    }

    Text(
        text = annotatedString,
        modifier = modifier,
        textAlign = TextAlign.Center
    )

}

@Preview
@Composable
fun LoadingViewPreview() {
    LoadingView()
}