package com.example.hydrocalculator.views

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import com.example.hydrocalculator.ui.theme.hydroGradient

@Composable
fun ConfirmationDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
) {

    AlertDialog(
        icon = {
            GradientIcon(
                icon = Icons.AutoMirrored.Filled.ExitToApp,
                contentDescription = "Exit App Icon",
                brush = MaterialTheme.hydroGradient
            )
        },
        title = {
            Text(
                text = dialogTitle,
                style = MaterialTheme.typography.titleLarge.copy(
                    brush = MaterialTheme.hydroGradient,
                    textAlign = TextAlign.Center,
                )
            )
        },
        text = {
            Text(
                text = dialogText,
                style = MaterialTheme.typography.titleMedium.copy(
                    brush = MaterialTheme.hydroGradient,
                    textAlign = TextAlign.Center,
                )
            )
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(
                    text = "Exit",
                    style = MaterialTheme.typography.titleSmall.copy(
                        brush = MaterialTheme.hydroGradient,
                        textAlign = TextAlign.Center
                    )
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(
                    text = "Cancel",
                    style = MaterialTheme.typography.titleSmall.copy(
                        brush = MaterialTheme.hydroGradient,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
    )

}

@Composable
fun GradientIcon(icon: ImageVector, contentDescription: String?, brush: Brush) {
    val painter = rememberVectorPainter(image = icon)
    val density = LocalDensity.current
    val intrinsicSizeDp = with(density) {
        painter.intrinsicSize.toDpSize()
    }

    Canvas(
        modifier = Modifier
            .size(intrinsicSizeDp)
            .graphicsLayer(alpha = 0.99f)
    ) {
        with(painter) {
            draw(size = this@Canvas.size)
        }

        drawRect(
            brush = brush,
            blendMode = BlendMode.SrcAtop
        )
    }
}