package com.example.hydrocalculator.calculationengine

import com.example.hydrocalculator.ui.views.screens.FocusedField

data class CalculationPressureUiState(
    val flowText: String = EMPTY_STRING,
    val diameterText: String = EMPTY_STRING,
    val velocity: Float = 0f,
    val headLoss: Float = 0f,
    val focusedField: FocusedField? = FocusedField.FLOW
)

private const val EMPTY_STRING = ""
