package com.example.hydrocalculator.views

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.hydrocalculator.R
import com.example.hydrocalculator.ui.theme.HydroCyan
import com.example.hydrocalculator.ui.theme.HydroGreen
import com.example.hydrocalculator.ui.theme.hydroGradient
import kotlinx.coroutines.delay

@Composable
fun LoadingAppView(
    modifier: Modifier = Modifier,
    text: String = stringResource(R.string.loading),
) {
    var visibleDotCount by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            visibleDotCount = (visibleDotCount + 1) % 4
            delay(700)
        }
    }

    val dotColors = listOf(
        HydroGreen,
        HydroCyan,
        Color.White
    )

    val annotatedString = buildAnnotatedString {
        append(text)

        dotColors.forEachIndexed { index, color ->
            val dotColor = if (index < visibleDotCount) color else Color.Transparent
            withStyle(
                style = SpanStyle(color = dotColor)) {
                append(".")
            }
        }
    }

    Text(
        text = annotatedString,
        modifier = modifier,
        textAlign = TextAlign.Center,
        style = TextStyle(
            brush = MaterialTheme.hydroGradient,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
    )
}

@Preview
@Composable
fun LoadingViewPreview() {
    LoadingAppView()
}