package com.example.hydrocalculator.ui.views

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hydrocalculator.ui.theme.HydroCyan


@Composable
fun NumericKeypad(
    modifier: Modifier = Modifier,
    onKeyClick: (String) -> Unit
) {
    val keys = listOf(
        "1", "2", "3",
        "4", "5", "6",
        "7", "8", "9",
        ".", "0", "BACKSPACE"
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        userScrollEnabled = false
    ) {
        items(keys) { key ->
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
    Surface(
        modifier = Modifier
            .aspectRatio(1.618f)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        color = HydroCyan,
    ) {
        Box(contentAlignment = Alignment.Center) {
            when (key) {
                "BACKSPACE" -> Icon(
                    Icons.Default.Backspace,
                    contentDescription = "Backspace",
                    tint = HydroCyan
                )

                "." -> Text(
                    text = key,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )

                else -> Text(text = key, style = MaterialTheme.typography.headlineLarge)
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
