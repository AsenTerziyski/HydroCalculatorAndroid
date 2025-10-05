package com.example.hydrocalculator

import android.R
import androidx.compose.animation.core.copy
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.hydrocalculator.ui.theme.hydroGradient
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HydroAppBar(title: String = "") {

    val fullTitle = title
    var visibleTitle by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        fullTitle.forEachIndexed { index, _ ->
            visibleTitle = fullTitle.substring(0, index + 1)
            delay(17)
        }
    }

    TopAppBar(
        title = {
            Text(
                text = visibleTitle,
                style = MaterialTheme.typography.titleLarge.copy(
                    brush = MaterialTheme.hydroGradient
                )
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}