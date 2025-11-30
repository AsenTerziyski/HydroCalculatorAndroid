package com.example.hydrocalculator.calculationengine

import com.example.hydrocalculator.utils.Resource

enum class FocusedField { FLOW, DIAMETER }

data class CalculationPressureUiState(
    val flowText: String = EMPTY_STRING,
    val diameterText: String = EMPTY_STRING,
    val velocity: Float = 0f,
    val headLoss: Float = 0f,
    val focusedField: FocusedField? = FocusedField.FLOW,
    val description: String = EMPTY_STRING,
    val saveOperationState: Resource<Unit> = Resource.Idle
)

private const val EMPTY_STRING = ""