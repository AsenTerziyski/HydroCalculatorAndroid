package com.asentt.hydrocalculator.ui.views.screens.pressure

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asentt.hydrocalculator.calculationengine.PressurePipeEngine
import com.asentt.hydrocalculator.domain.usecase.SaveCalculationUseCase
import com.asentt.hydrocalculator.ui.views.snackbar.SnackBarEvent
import com.asentt.hydrocalculator.utils.Resource
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
@Inject constructor(
    private val pressurePipeEngine: PressurePipeEngine,
    private val saveCalculationUseCase: SaveCalculationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalculationPressureUiState())
    val uiState: StateFlow<CalculationPressureUiState> = _uiState.asStateFlow()

    private val _showDialogChannel = Channel<SaveDialogEvent>()
    val showDialogChannel = _showDialogChannel.receiveAsFlow()

    private val _snackBarEventChannel = Channel<SnackBarEvent>()
    val snackBarEventChannel = _snackBarEventChannel.receiveAsFlow()


    fun onKeyClick(key: String) {
        val currentState = _uiState.value
        val currentText = when (currentState.focusedField) {
            FocusedField.FLOW -> currentState.flowText
            FocusedField.DIAMETER -> currentState.diameterText
            FocusedField.ROUGHNESS -> currentState.roughnessText
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
            when (state.focusedField) {
                FocusedField.FLOW -> {
                    state.copy(flowText = newText)
                }

                FocusedField.DIAMETER -> {
                    state.copy(diameterText = newText)
                }

                else -> {
                    state.copy(roughnessText = newText)
                }
            }
        }
        recalculateResults()
    }

    private fun recalculateResults() {
        val currentState = _uiState.value
        val waterFlow = currentState.flowText.toFloatOrNull() ?: 0f
        val roughness = currentState.roughnessText.toFloatOrNull() ?: 0f
        val pipeDiameter = getEffectiveDiameter(currentState)
        val (newVelocity, newHeadLoss)
                = calculateVelocityAndHeadLoss(waterFlow, pipeDiameter, roughness)
        if (currentState.velocity != newVelocity || currentState.headLoss != newHeadLoss) {
            _uiState.update { state ->
                state.copy(velocity = newVelocity, headLoss = newHeadLoss)
            }
        }
    }

    private fun calculateVelocityAndHeadLoss(
        waterFlow: Float,
        pipeDiameter: Float,
        roughness: Float
    ): Pair<Float, Float> {
        return if (waterFlow > 0 && pipeDiameter > 0 && roughness > 0) {
            val velocity = pressurePipeEngine.estimateVelocity(waterFlow, pipeDiameter, roughness)
            val headLoss = pressurePipeEngine.estimateHeadloss(waterFlow, velocity, roughness)
            velocity to headLoss
        } else {
            0f to 0f
        }
    }

    private fun getEffectiveDiameter(state: CalculationPressureUiState): Float {
        val pn = state.pressureRating
        return if (pn != null) {
            when (pn) {
                PressureRating.PN10 -> state.catalogPipe.idPn10.toFloat()
                PressureRating.PN16 -> state.catalogPipe.idPn16.toFloat()
            }
        } else {
            state.diameterText.toFloatOrNull() ?: 0f
        }
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

            val cleanedRoughnessText = if (newFocus != FocusedField.ROUGHNESS) {
                currentState.roughnessText.removeSuffix(".")
            } else {
                currentState.roughnessText
            }

            currentState.copy(
                focusedField = newFocus,
                flowText = cleanedFlowText,
                diameterText = cleanedDiameterText,
                roughnessText = cleanedRoughnessText
            )
        }
    }

    fun onSaveIntent() {
        viewModelScope.launch {
            _showDialogChannel.send(SaveDialogEvent.Show)
        }
    }

    fun onDescriptionChange(description: String) {
        _uiState.update { state -> state.copy(description = description) }
    }

    fun onConfirmSave() {
        viewModelScope.launch {
            _showDialogChannel.send(SaveDialogEvent.Hide)
            _uiState.update { it.copy(saveState = Resource.Loading) }
            val currentState = _uiState.value

            val diameter = getEffectiveDiameter(currentState)
            val waterFlow = currentState.flowText.toFloatOrNull() ?: 0f
            val roughness = currentState.roughnessText.toFloatOrNull() ?: 0f

            if (waterFlow <= 0f || diameter <= 0f || roughness <= 0f) {
                val errorMsg = "Inputs must be greater than 0"
                _uiState.update {
                    it.copy(saveState = Resource.Error(Exception(errorMsg)))
                }
                _snackBarEventChannel.send(SnackBarEvent.ShowSnackBar(errorMsg))
                return@launch
            }

            val result = saveCalculationUseCase(
                waterFlow = waterFlow,
                pipeDiameter = diameter,
                roughness = roughness,
                velocity = currentState.velocity,
                headLoss = currentState.headLoss,
                description = currentState.description
            )

            when (result) {
                is Resource.Success<*> -> {
                    _uiState.update { state ->
                        state.copy(
                            description = "",
                            saveState = Resource.Success(Unit)
                        )
                    }
                    _snackBarEventChannel.send(SnackBarEvent.ShowSnackBar("Calculation saved successfully"))
                }
                is Resource.Error -> {
                    val exception = result.exeption
                    _snackBarEventChannel.send(SnackBarEvent.ShowSnackBar(exception.message ?: "Unknown Error"))
                    _uiState.update { state ->
                        state.copy(saveState = Resource.Error(exception))
                    }
                }
                else -> { /* Ignore Idle/Loading */ }
            }
        }
    }

    fun onDismissDialog() {
        _uiState.update { state -> state.copy(description = "") }
        viewModelScope.launch {
            _showDialogChannel.send(SaveDialogEvent.Hide)
        }
    }

    fun onClearInputFields() {
        _uiState.update {
            it.copy(
                flowText = "0",
                diameterText = "0",
                roughnessText = "0",
                velocity = 0f,
                headLoss = 0f,
                description = "",
                catalogPipe = CatalogPipes.DN32,
                pressureRating = null,
                saveState = Resource.Idle,
                focusedField = FocusedField.FLOW
            )
        }
    }

    fun onPressureRatingSelected(newRating: PressureRating?) {
        _uiState.update { state ->
            state.copy(pressureRating = newRating)
        }
        recalculateResults()
    }

    fun onCatalogPipeSelected(newPipe: CatalogPipes) {
        _uiState.update { it.copy(catalogPipe = newPipe) }
        recalculateResults()
    }

}

sealed interface SaveDialogEvent {
    data object Show : SaveDialogEvent
    data object Hide : SaveDialogEvent
}