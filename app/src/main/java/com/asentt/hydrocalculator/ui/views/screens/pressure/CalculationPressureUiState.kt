package com.asentt.hydrocalculator.ui.views.screens.pressure

import com.asentt.hydrocalculator.utils.Resource

enum class FocusedField { FLOW, DIAMETER, ROUGHNESS, NONE }

data class CalculationPressureUiState(
    val flowText: String = EMPTY_STRING,
    val diameterText: String = EMPTY_STRING,
    val roughnessText: String = EMPTY_STRING,
    val velocity: Float = 0f,
    val headLoss: Float = 0f,
    val description: String = EMPTY_STRING,
    val focusedField: FocusedField? = FocusedField.FLOW,
    val saveState: Resource<Unit> = Resource.Idle
)

private const val EMPTY_STRING = ""