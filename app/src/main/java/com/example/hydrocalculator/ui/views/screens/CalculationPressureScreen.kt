package com.example.hydrocalculator.ui.views.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.hydrocalculator.ui.views.NumericKeypad

@Composable
fun CalculationPressureScreen(onNavigateBack: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        NumericKeypad {  }
    }
}

@Preview(showBackground = true)
@Composable
private fun CalculationResultsScreenPreview() {
    CalculationPressureScreen(onNavigateBack = {})
}