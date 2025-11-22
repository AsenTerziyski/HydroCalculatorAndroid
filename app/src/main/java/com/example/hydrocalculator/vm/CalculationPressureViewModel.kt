package com.example.hydrocalculator.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hydrocalculator.calculationengine.CalculationPressureUiState
import com.example.hydrocalculator.calculationengine.FocusedField
import com.example.hydrocalculator.calculationengine.PressurePipeEngine
import com.example.hydrocalculator.ui.views.screens.CalculationPressureScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalculationPressureViewModel
@Inject constructor(private val pressurePipeEngine: PressurePipeEngine) : ViewModel() {

    private val _uiState = MutableStateFlow(CalculationPressureUiState())
    val uiState: StateFlow<CalculationPressureUiState> = _uiState.asStateFlow()

    private val _eventChannel = Channel<CalculationPressureEvent>()
    val eventChannel = _eventChannel.receiveAsFlow()

    fun onKeyClick(key: String) {
        val currentState = _uiState.value
        val currentText = when (currentState.focusedField) {
            FocusedField.FLOW -> currentState.flowText
            FocusedField.DIAMETER -> currentState.diameterText
            else -> return
        }

        val newText = when (key) {
            "BACKSPACE" -> currentText.dropLast(1)
            "." -> if (currentText.contains(".")) currentText else if (currentText.isEmpty()) "0." else "$currentText."
            else -> if (currentText.length < 8) currentText + key else currentText
        }

        _uiState.update { state ->
            if (state.focusedField == FocusedField.FLOW) {
                state.copy(flowText = newText)
            } else {
                state.copy(diameterText = newText)
            }
        }
        recalculateResults()
    }

    fun onFocusChanged(newFocus: FocusedField) {
        _uiState.update { currentState ->
            val cleanedFlowText = if (newFocus != FocusedField.FLOW) {
                currentState.flowText.removeSuffix(".")
            } else {
                currentState.flowText
            }

            val cleanedDiameterText = if (newFocus != FocusedField.DIAMETER) {
                currentState.diameterText.removeSuffix(".")
            } else {
                currentState.diameterText
            }

            currentState.copy(
                focusedField = newFocus,
                flowText = cleanedFlowText,
                diameterText = cleanedDiameterText
            )
        }
    }

    private fun recalculateResults() {
        val waterFlow = _uiState.value.flowText.toFloatOrNull() ?: 0f
        val pipeDiameter = _uiState.value.diameterText.toFloatOrNull() ?: 0f

        if (waterFlow > 0 && pipeDiameter > 0) {
            val velocityResult = pressurePipeEngine.estimateVelocity(waterFlow, pipeDiameter)
            val headLossResult = waterFlow / pipeDiameter // Dummy formula
            _uiState.update { state ->
                state.copy(velocity = velocityResult, headLoss = headLossResult)
            }
        } else {
            _uiState.update { state ->
                state.copy(velocity = 0f, headLoss = 0f)
            }
        }
    }

    fun onSaveIntent() {
        viewModelScope.launch {
            _eventChannel.send(CalculationPressureEvent.ShowSaveDialog)
        }
    }

    fun onDescriptionChange(description: String) {
        _uiState.update { state -> state.copy(description = description) }
    }

    fun onConfirmSave() {
        val currentState = _uiState.value
        _uiState.update { state -> state.copy(description = "") }
        viewModelScope.launch {
            _eventChannel.send(CalculationPressureEvent.HideSaveDialog)
        }
    }

    fun onDismissDialog() {
        _uiState.update { state -> state.copy(description = "") }
        viewModelScope.launch {
            _eventChannel.send(CalculationPressureEvent.HideSaveDialog)
        }
    }

}

sealed interface CalculationPressureEvent {
    data object ShowSaveDialog : CalculationPressureEvent
    data object HideSaveDialog : CalculationPressureEvent
}