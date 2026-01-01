package com.asentt.hydrocalculator.ui.views.screens.pressure

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.asentt.hydrocalculator.ui.views.snackbar.SnackBarToastView
import com.asentt.hydrocalculator.ui.theme.HydroCyan
import com.asentt.hydrocalculator.ui.views.NumericKeypad
import com.asentt.hydrocalculator.ui.views.SavingView
import com.asentt.hydrocalculator.ui.views.dialogs.SaveCalculationDialog
import com.asentt.hydrocalculator.ui.views.snackbar.SnackBarEvent
import com.asentt.hydrocalculator.utils.Resource
import kotlinx.coroutines.launch

@Composable
fun CalculationPressureScreen(viewModel: CalculationPressureViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    var isCatalogOptionSelected by remember { mutableStateOf(false) }

    var isSaveResultDialogVisible by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = Unit) {
        viewModel.showDialogChannel.collect { event ->
            isSaveResultDialogVisible = when (event) {
                SaveDilaogEvent.Hide -> {
                    false
                }

                SaveDilaogEvent.Show -> {
                    true
                }
            }
        }
    }

    if (isSaveResultDialogVisible) {
        SaveCalculationDialog(
            description = uiState.description,
            onDescriptionChange = viewModel::onDescriptionChange,
            onConfirm = {
                viewModel.onConfirmSave(
                    isOptionSelected = isCatalogOptionSelected
                )
                viewModel.onClearInputFields()
            },
            onDismiss = viewModel::onDismissDialog
        )
    }

    val snackBarHostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = Unit) {
        viewModel.snackBarEventChannel.collect { snackBarEvent ->
            when (snackBarEvent) {
                is SnackBarEvent.ShowSnackBar -> {
                    scope.launch {
                        snackBarHostState.showSnackbar(
                            message = snackBarEvent.message,
                            withDismissAction = true
                        )
                    }
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackBarToastView(snackBarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        when (uiState.saveState) {
            Resource.Loading -> {
                SavingView()
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 28.dp)
                                .padding(top = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            UnitInputField(
                                value = uiState.flowText,
                                label = "Flow",
                                unit = "l/s",
                                isFocused = uiState.focusedField == FocusedField.FLOW,
                                onFocus = { viewModel.onFocusChanged(FocusedField.FLOW) }
                            )
                            if (isCatalogOptionSelected) {
                                CatalogSelectionRow(
                                    selectedPipe = uiState.catalogPipe,
                                    selectedPN = uiState.pressureRating,
                                    onPipeSelected = {
                                        viewModel.onCatalogPipeSelected(it)
                                    },
                                    onPNSelected = {
                                        viewModel.onPressureRatingSelected(it)
                                    }
                                )
                            } else {
                                UnitInputField(
                                    value = uiState.diameterText,
                                    label = "Diameter",
                                    unit = "mm",
                                    isFocused = uiState.focusedField == FocusedField.DIAMETER,
                                    onFocus = { viewModel.onFocusChanged(FocusedField.DIAMETER) }
                                )
                            }

                            UnitInputField(
                                value = uiState.roughnessText,
                                label = "Roughness",
                                unit = "m",
                                isFocused = uiState.focusedField == FocusedField.ROUGHNESS,
                                onFocus = { viewModel.onFocusChanged(FocusedField.ROUGHNESS) }
                            )
                            val velocity = uiState.velocity
                            val velocityWarningText =
                                if (velocity > 2.5) "Velocity > 2.5"
                                else if (velocity > 0 && velocity < 0.3) "Velocity < 0.3"
                                else null

                            ResultField(
                                label = "Velocity",
                                value = "%.2f".format(uiState.velocity),
                                unit = "m/s",
                                warningMessage = velocityWarningText
                            )
                            ResultField(
                                label = "Head loss",
                                value = "%.4f".format(uiState.headLoss),
                                unit = "m/m"
                            )
                        }
                    }

                    NumericKeypad { key -> viewModel.onKeyClick(key) }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 28.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val optionButtonTitle =
                            if (isCatalogOptionSelected) "By CATALOG" else "By INPUT"
                        HydroActionButton(
                            modifier = Modifier
                                .weight(0.4f)
                                .padding(vertical = 12.dp),
                            title = optionButtonTitle,
                            textColor = Color.White
                        ) {
                            isCatalogOptionSelected = !isCatalogOptionSelected
                        }
                        HydroActionButton(
                            modifier = Modifier
                                .weight(0.6f)
                                .padding(vertical = 12.dp),
                            textColor = Color.Red
                        ) {
                            viewModel.apply {
                                onSaveIntent()
                                onFocusChanged(FocusedField.NONE)
                            }
                        }
                    }

                }
            }
        }
    }
}

@Composable
private fun UnitInputField(
    value: String,
    label: String,
    unit: String,
    isFocused: Boolean,
    onFocus: () -> Unit
) {
    val borderColor =
        if (isFocused) HydroCyan else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, borderColor, RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onFocus)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = borderColor)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = value.ifEmpty { "0" },
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Start,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = unit,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun ResultField(
    label: String,
    value: String,
    unit: String,
    warningMessage: String? = null
) {
    val contentColor = if (warningMessage != null) MaterialTheme.colorScheme.error else HydroCyan
    val displayValue = if (value.length > 7) {
        value.take(7) + "..."
    } else {
        value
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
            if (warningMessage != null) {
                Text(
                    text = warningMessage,
                    color = Color.Red,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.End
                )
            }
        }

        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = displayValue,
                style = MaterialTheme.typography.headlineSmall,
                color = contentColor
            )
            Spacer(modifier = Modifier.padding(start = 4.dp))
            Text(
                text = unit,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun HydroActionButton(
    modifier: Modifier = Modifier,
    title: String = "SAVE",
    textColor: Color = HydroCyan,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val backgroundColor by animateColorAsState(
        targetValue = if (isPressed) HydroCyan.copy(alpha = 0.5f) else Color.Black,
        label = "backgroundColor"
    )

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.7f else 1f,
        label = "scale"
    )

    Surface(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .height(48.dp)
            .border(
                width = 2.dp,
                color = HydroCyan.copy(alpha = 0.5f),
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        color = backgroundColor,
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = title,
                color = textColor,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Preview
@Composable
fun SaveButtonPreview() {
    HydroActionButton(onClick = {})
}
