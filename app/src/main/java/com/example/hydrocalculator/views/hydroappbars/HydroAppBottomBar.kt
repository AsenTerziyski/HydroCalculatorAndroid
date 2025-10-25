package com.example.hydrocalculator.views.hydroappbars

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hydrocalculator.ui.theme.hydroGradient

@Composable
fun HydroAppBottomBar(

) {
    Box(
        modifier = Modifier.background(
            brush = MaterialTheme.hydroGradient
        )
    ) {
        BottomAppBar(
            containerColor = Color.Transparent,
        ) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = "test"
            )

            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = "test"
            )

            IconButton(
                modifier = Modifier.weight(1f),
                onClick = {}) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Filled.PowerSettingsNew,
                        contentDescription = "Switch Off",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Switch Off",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = "test"
            )

            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = "test"
            )
        }
    }
}

@Preview
@Composable
fun HydroAppBottomBarPreview() {
    HydroAppBottomBar()
}