package com.asentt.hydrocalculator.ui.views

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asentt.hydrocalculator.ui.theme.HydroCyan


@Composable
fun NumericKeypad(
    modifier: Modifier = Modifier,
    onKeyClick: (String) -> Unit
) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        userScrollEnabled = false
    ) {
        items(numericPadKeys) { key ->
            KeyButton(
                key = key,
                onClick = { onKeyClick(key) }
            )
        }
    }
}

@Composable
private fun KeyButton(
    key: String,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val backgroundColor by animateColorAsState(
        targetValue = if (isPressed) HydroCyan.copy(alpha = 0.5f) else Color.Black,
        label = "backgroundColor"
    )

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.7f else 1f,
        label = "scale"
    )

    Surface(
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .aspectRatio(2.5f)
            .border(
                width = 2.dp,
                color = HydroCyan.copy(alpha = 0.5f),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick),
        color = backgroundColor,
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            when (key) {
                "BACKSPACE" -> Icon(
                    Icons.AutoMirrored.Filled.Backspace,
                    contentDescription = "Backspace",
                    tint = HydroCyan
                )

                "." -> Text(
                    text = key,
                    style = MaterialTheme.typography.headlineMedium,
                    color = HydroCyan,
                    fontWeight = FontWeight.Bold
                )

                else -> Text(
                    text = key,
                    color = HydroCyan,
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    }
}

@Preview
@Composable
fun NumericKeypadPreview() {
    NumericKeypad(onKeyClick = {})
}

@Preview
@Composable
fun KeyButtonPreview() {
    KeyButton("7", {})
}

private val numericPadKeys = listOf(
    "1", "2", "3",
    "4", "5", "6",
    "7", "8", "9",
    ".", "0", "BACKSPACE"
)