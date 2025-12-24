package com.asentt.hydrocalculator.ui.views.screens.pressure

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asentt.hydrocalculator.calculationengine.PressurePipeEngine
import com.asentt.hydrocalculator.domain.usecase.SaveCalculationUseCase
import com.asentt.hydrocalculator.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalculationPressureViewModel
@Inject constructor(
    private val pressurePipeEngine: PressurePipeEngine,
    private val saveCalculationUseCase: SaveCalculationUseCase
) : ViewModel() {

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
            else -> {
                val isEditingANumber = key.all { it.isDigit() }

                when {
                    currentText == "0" && isEditingANumber -> key
                    currentText.length < 8 -> currentText + key
                    else -> currentText
                }
            }
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
            val headLossResult = pressurePipeEngine.estimateHeadloss(waterFlow, velocityResult)
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

        viewModelScope.launch {
            _eventChannel.send(CalculationPressureEvent.HideSaveDialog)

            val currentState = _uiState.value

            _uiState.update { state -> state.copy(saveOperationState = Resource.Loading) }

            if ((currentState.flowText.isEmpty() || currentState.diameterText.isEmpty()) ||
                currentState.flowText == "0" || currentState.diameterText == "0"
            ) {
                _uiState.update {
                    it.copy(saveOperationState = Resource.Error(Exception("Flow and diameter cannot be empty")))
                }
                _eventChannel.send(CalculationPressureEvent.ShowSnackBar("Flow and diameter cannot be empty"))
            } else {
                val result = saveCalculationUseCase(
                    waterFlow = currentState.flowText.toFloat(),
                    pipeDiameter = currentState.diameterText.toFloat(),
                    velocity = currentState.velocity,
                    headLoss = currentState.headLoss,
                    description = currentState.description
                )

                when (result) {
                    Resource.Idle -> {}

                    Resource.Loading -> {
                        _uiState.update { state -> state.copy(saveOperationState = Resource.Loading) }
                    }

                    is Resource.Success<*> -> {
                        _uiState.update { state ->
                            state.copy(
                                description = currentState.description,
                                saveOperationState = Resource.Success(Unit)
                            )
                        }
                        delay(1000)
                        _eventChannel.send(CalculationPressureEvent.ShowSnackBar("Calculation saved successfully"))
                    }

                    is Resource.Error -> {
                        val exception = result.exeption
                        _eventChannel.send(
                            CalculationPressureEvent.ShowSnackBar(
                                exception.message ?: "Unknown Error"
                            )
                        )
                        _uiState.update { state ->
                            state.copy(saveOperationState = Resource.Error(exception))
                        }
                    }
                }
            }
        }
    }

    fun resetSaveState() {
        _uiState.update { state -> state.copy(saveOperationState = Resource.Idle) }
    }

    fun onDismissDialog() {
        _uiState.update { state -> state.copy(description = "") }
        viewModelScope.launch {
            _eventChannel.send(CalculationPressureEvent.HideSaveDialog)
        }
    }

    fun onClearInputFields() {
        _uiState.update {
            it.copy(
                flowText = "0",
                diameterText = "0",
                velocity = 0f,
                headLoss = 0f,
                description = "",
                saveOperationState = Resource.Idle,
                focusedField = FocusedField.FLOW
            )
        }
    }

}

sealed interface CalculationPressureEvent {
    data object ShowSaveDialog : CalculationPressureEvent
    data object HideSaveDialog : CalculationPressureEvent
    data class ShowSnackBar(val message: String) : CalculationPressureEvent
}