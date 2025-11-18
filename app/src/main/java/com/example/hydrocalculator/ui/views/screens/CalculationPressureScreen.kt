package com.example.hydrocalculator.ui.views.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hydrocalculator.calculationengine.PressurePipeEngine
import com.example.hydrocalculator.ui.theme.HydroCyan
import com.example.hydrocalculator.ui.views.NumericKeypad
import com.example.hydrocalculator.vm.CalculationPressureViewModel

enum class FocusedField { FLOW, DIAMETER }

@Composable
fun CalculationPressureScreen(
    viewModel: CalculationPressureViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
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
                    onFocus = { viewModel.onFocusChanged(FocusedField.DIAMETER)}
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

@Preview(showBackground = true)
@Composable
private fun CalculationResultsScreenPreview() {
    CalculationPressureScreen()
}
