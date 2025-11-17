package com.example.hydrocalculator.ui.views.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.hydrocalculator.ui.theme.HydroCyan
import com.example.hydrocalculator.ui.views.NumericKeypad

private enum class FocusedField { FLOW, DIAMETER }

@Composable
fun CalculationPressureScreen() {

    var flowText by remember { mutableStateOf("") }
    var diameterText by remember { mutableStateOf("") }
    var focusedField by remember { mutableStateOf(FocusedField.FLOW) }

    LaunchedEffect(focusedField) {
        if (focusedField != FocusedField.FLOW) {
            flowText = flowText.removeSuffix(".")
        }
        if (focusedField != FocusedField.DIAMETER) {
            diameterText = diameterText.removeSuffix(".")
        }
    }

    val resultVelocity by remember {
        derivedStateOf {
            val flow = flowText.toFloatOrNull() ?: 0f
            val diameter = diameterText.toFloatOrNull() ?: 0f
            if (flow > 0 && diameter > 0) flow * diameter else 0f
        }
    }
    val resultHeadLoss by remember {
        derivedStateOf {
            val flow = flowText.toFloatOrNull() ?: 0f
            val diameter = diameterText.toFloatOrNull() ?: 0f
            if (flow > 0 && diameter > 0) flow / diameter else 0f
        }
    }

    val onKeyClick: (String) -> Unit = { key ->
        val currentText = if (focusedField == FocusedField.FLOW) flowText else diameterText

        val newText = when (key) {
            "BACKSPACE" -> currentText.dropLast(1)
            "." -> if (currentText.contains(".")) currentText else if (currentText.isEmpty()) "0." else "$currentText."
            else -> if (currentText.length < 8) currentText + key else currentText
        }

        if (focusedField == FocusedField.FLOW) {
            flowText = newText
        } else {
            diameterText = newText
        }

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
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
                value = flowText,
                label = "Flow",
                unit = "l/s",
                isFocused = focusedField == FocusedField.FLOW,
                onFocus = { focusedField = FocusedField.FLOW }
            )
            UnitInputField(
                value = diameterText,
                label = "Diameter",
                unit = "mm",
                isFocused = focusedField == FocusedField.DIAMETER,
                onFocus = { focusedField = FocusedField.DIAMETER }
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            ResultField(label = "Velocity", value = "%.2f".format(resultVelocity), unit = "m/s")
            ResultField(label = "Head loss", value = "%.4f".format(resultHeadLoss), unit = "m/m")
        }

        Spacer(modifier = Modifier.weight(1f))

        NumericKeypad { key -> onKeyClick(key) }
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
