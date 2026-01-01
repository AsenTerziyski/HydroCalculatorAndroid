package com.asentt.hydrocalculator.ui.views.screens.pressure

import android.util.Log
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

    private val _showDialogChannel = Channel<SaveDilaogEvent>()
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
        val waterFlow = _uiState.value.flowText.toFloatOrNull() ?: 0f
        val pipeDiameter = _uiState.value.diameterText.toFloatOrNull() ?: 0f
        val roughness = _uiState.value.roughnessText.toFloatOrNull() ?: 0f

        if (waterFlow > 0 && pipeDiameter > 0 && roughness > 0) {
            val velocityResult =
                pressurePipeEngine.estimateVelocity(waterFlow, pipeDiameter, roughness)
            val headLossResult =
                pressurePipeEngine.estimateHeadloss(waterFlow, velocityResult, roughness)
            _uiState.update { state ->
                state.copy(velocity = velocityResult, headLoss = headLossResult)
            }
        } else {
            _uiState.update { state ->
                state.copy(velocity = 0f, headLoss = 0f)
            }
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
            _showDialogChannel.send(SaveDilaogEvent.Show)
        }
    }

    fun onDescriptionChange(description: String) {
        _uiState.update { state -> state.copy(description = description) }
    }

    fun onConfirmSave(isOptionSelected: Boolean = false) {

        viewModelScope.launch {
            _showDialogChannel.send(SaveDilaogEvent.Hide)

            val currentState = _uiState.value
            val pn = currentState.pressureRating
            val dn = currentState.catalogPipe
            var diameter: String?

            if (isOptionSelected) {
                diameter = when (pn) {
                    PressureRating.PN10 -> {
                        dn?.idPn10.toString()
                    }

                    PressureRating.PN16 -> {
                        dn?.idPn16.toString()
                    }

                    null -> {
                        ""
                    }
                }
            } else {
                diameter = currentState.diameterText
            }

            Log.d("TAG101", "DIAMETER!!!" + diameter)

            _uiState.update { state -> state.copy(saveState = Resource.Loading) }

            if ((currentState.flowText.isEmpty()
                        || diameter.isEmpty()
                        || currentState.roughnessText.isEmpty())
                ||
                (currentState.flowText == "0"
                        || diameter == "0"
                        || currentState.roughnessText == "0")
            ) {
                _uiState.update {
                    it.copy(saveState = Resource.Error(Exception("Inputs cannot be 0")))
                }
                _snackBarEventChannel.send(SnackBarEvent.ShowSnackBar("Inputs cannot be 0"))
            } else {
                val result = saveCalculationUseCase(
                    waterFlow = currentState.flowText.toFloat(),
                    pipeDiameter = diameter.toFloat(),
                    roughness = currentState.roughnessText.toFloat(),
                    velocity = currentState.velocity,
                    headLoss = currentState.headLoss,
                    description = currentState.description
                )

                when (result) {
                    Resource.Idle -> {}

                    Resource.Loading -> {
                        _uiState.update { state -> state.copy(saveState = Resource.Loading) }
                    }

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
                        _snackBarEventChannel.send(
                            SnackBarEvent.ShowSnackBar(
                                exception.message ?: "Unknown Error"
                            )
                        )
                        _uiState.update { state ->
                            state.copy(saveState = Resource.Error(exception))
                        }
                    }
                }
            }
        }
    }

    fun onDismissDialog() {
        _uiState.update { state -> state.copy(description = "") }
        viewModelScope.launch {
            _showDialogChannel.send(SaveDilaogEvent.Hide)
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
                saveState = Resource.Idle,
                focusedField = FocusedField.FLOW
            )
        }
    }

    fun onPressureRatingSelected(newRating: PressureRating) {
        _uiState.update { state ->
            state.copy(
                pressureRating = newRating,
                focusedField = FocusedField.CATALOG_DIAMETER
            )
        }
    }

    fun onCatalogPipeSelected(newPipe: CatalogPipes) {
        _uiState.update {
            it.copy(
                catalogPipe = newPipe,
                focusedField = FocusedField.CATALOG_DIAMETER
            )
        }
    }


}

sealed interface SaveDilaogEvent {
    data object Show : SaveDilaogEvent
    data object Hide : SaveDilaogEvent
}