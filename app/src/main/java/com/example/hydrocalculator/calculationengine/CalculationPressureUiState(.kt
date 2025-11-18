package com.example.hydrocalculator.calculationengine

import com.example.hydrocalculator.ui.views.screens.FocusedField

data class CalculationPressureUiState(
    val flowText: String = "",
    val diameterText: String = "",
    val velocity: Float = 0f,
    val headLoss: Float = 0f,
    val focusedField: FocusedField? = FocusedField.FLOW
)
