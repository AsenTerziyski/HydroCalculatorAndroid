package com.asentt.hydrocalculator.ui.views.screens.pressurecalculation

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
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
import com.asentt.hydrocalculator.ui.theme.HydroCyan
import com.asentt.hydrocalculator.ui.views.NumericKeypad
import com.asentt.hydrocalculator.ui.views.SavingView
import com.asentt.hydrocalculator.ui.views.dialogs.SaveCalculationDialog
import com.asentt.hydrocalculator.utils.Resource
import kotlinx.coroutines.launch

@Composable
fun CalculationPressureScreen(
    viewModel: CalculationPressureViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var isSaveResultDialogVisible by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        viewModel.eventChannel.collect { event ->
            isSaveResultDialogVisible = when (event) {
                is CalculationPressureEvent.ShowSaveDialog -> {
                    true
                }

                CalculationPressureEvent.HideSaveDialog -> {
                    false
                }
            }
        }
    }

    if (isSaveResultDialogVisible) {
        SaveCalculationDialog(
            description = uiState.description,
            onDescriptionChange = viewModel::onDescriptionChange,
            onConfirm = viewModel::onConfirmSave,
            onDismiss = viewModel::onDismissDialog
        )
    }

    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = uiState.saveOperationState) {
        when (uiState.saveOperationState) {
            is Resource.Error -> {
                val message = (uiState.saveOperationState as Resource.Error).exeption.message
                scope.launch {
                    snackBarHostState.showSnackbar(
                        message = message.toString(),
                        withDismissAction = true
                    )
                }
                viewModel.resetSaveState()
            }

            is Resource.Success<*> -> {
                scope.launch {
                    snackBarHostState.showSnackbar(message = "Calculation saved successfully")
                    viewModel.resetSaveState()
                }
            }

            else -> {}
        }
    }

    Scaffold(
        snackbarHost = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                SnackbarHost(hostState = snackBarHostState) { data ->
                    Snackbar(
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .border(
                                width = 2.dp,
                                color = HydroCyan.copy(alpha = 0.8f),
                                shape = RoundedCornerShape(12.dp)
                            ),
                        containerColor = Color.Black,
                        contentColor = HydroCyan,
                    ) { Text(data.visuals.message) }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        when (uiState.saveOperationState) {
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
                            UnitInputField(
                                value = uiState.diameterText,
                                label = "Diameter",
                                unit = "mm",
                                isFocused = uiState.focusedField == FocusedField.DIAMETER,
                                onFocus = { viewModel.onFocusChanged(FocusedField.DIAMETER) }
                            )
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                            ResultField(
                                label = "Velocity",
                                value = "%.2f".format(uiState.velocity),
                                unit = "m/s"
                            )
                            ResultField(
                                label = "Head loss",
                                value = "%.4f".format(uiState.headLoss),
                                unit = "m/m"
                            )
                        }
                    }

                    NumericKeypad { key -> viewModel.onKeyClick(key) }

                    SaveButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 28.dp)
                            .padding(vertical = 12.dp),
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
private fun ResultField(label: String, value: String, unit: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )
        Row(verticalAlignment = Alignment.Bottom) {
            Text(text = value, style = MaterialTheme.typography.headlineSmall, color = HydroCyan)
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
private fun SaveButton(
    modifier: Modifier = Modifier,
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
                text = "Save Result",
                color = HydroCyan,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Preview
@Composable
fun SaveButtonPreview() {
    SaveButton(onClick = {})
}
