package com.asentt.hydrocalculator.ui.views.screens.calculationtype

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asentt.hydrocalculator.R
import com.asentt.hydrocalculator.ui.theme.HydroCyan
import com.asentt.hydrocalculator.ui.theme.HydroGreen

@Composable
fun CalculationTypeCard(
    title: String,
    description: String,
    hasBadge: Boolean = false,
    badgeCount: Int = 0,
    onClick: () -> Unit
) {
    var targetRotation by remember { mutableFloatStateOf(-0.1f) }
    val rotation by animateFloatAsState(
        targetValue = targetRotation,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "trembleAnimation"
    )
    LaunchedEffect(Unit) { targetRotation = 1f }

    val cardShape = RoundedCornerShape(28.dp)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .graphicsLayer { this.rotationZ = rotation }
            .clipToBounds()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .shadow(elevation = 4.dp, shape = cardShape)
                .border(width = 2.dp, color = HydroCyan, shape = cardShape)
                .clip(cardShape)
                .clickable(onClick = onClick)
        ) {
            Image(
                painter = painterResource(id = R.drawable.hydro_calc_logo),
                contentDescription = null,
                contentScale = ContentScale.Inside,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black,
                                HydroGreen.copy(alpha = 0.5f),
                                HydroCyan.copy(alpha = 0.3f),
                                Color.Black,
                                Color.Transparent,
                            ),
                            startY = 100f
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 32.dp, horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                if (hasBadge) {
                    BadgedBox(
                        badge = {
                            if (badgeCount > 0) {
                                Badge(
                                    modifier = Modifier.padding(bottom = 6.dp),
                                    containerColor = Color.Red,
                                    contentColor = Color.White
                                ) {
                                    val badgeText = badgeCount.toString()
                                    Text(
                                        text = badgeText,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = Color.White
                        )
                    }
                } else {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.padding(12.dp))

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun CalculationTypeCardPreview() {
    CalculationTypeCard(
        onClick = {},
        title = "Pressurized Pipes",
        description = "Estimate flow and velocity in pipes under pressure"
    )
}