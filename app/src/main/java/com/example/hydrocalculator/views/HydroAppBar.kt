package com.example.hydrocalculator.views

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.hydrocalculator.ui.theme.HydroCyan
import com.example.hydrocalculator.ui.theme.HydroGreen
import com.example.hydrocalculator.ui.theme.hydroGradient
import com.example.hydrocalculator.utils.UseTextAnimation
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HydroAppBar(
    title: String = "",
    onBackPress: (() -> Unit)? = null
) {

    val (visibleTitle, animatedFontSize) = UseTextAnimation(
        text = title,
        minFontSize = 7.sp,
        maxFontSize = MaterialTheme.typography.titleLarge.fontSize
    )

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val iconColor = if (isPressed) HydroGreen else HydroCyan

    TopAppBar(
        title = {
            Text(
                text = visibleTitle,
                style = MaterialTheme.typography.titleLarge.copy(
                    brush = MaterialTheme.hydroGradient,
                    fontSize = animatedFontSize
                )
            )
        },
        navigationIcon = {
            if (onBackPress != null) {
                IconButton(
                    onClick = onBackPress,
                    interactionSource = interactionSource
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = iconColor
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}