package com.example.hydrocalculator.ui.views.dialogs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.material3.TextButton
import androidx.compose.ui.tooling.preview.Preview

@Composable
private fun SaveCalculationDialog(
    description: String,
    onDescriptionChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Save Calculation") },
        text = {
            OutlinedTextField(
                value = description,
                onValueChange = onDescriptionChange,
                label = { Text("Description (Optional)") },
                placeholder = { Text("e.g., Main water line") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Preview
@Composable
fun SaveResultDialogPreview() {
    SaveCalculationDialog(
        description = "Main water line",
        onDescriptionChange = {},
        onConfirm = {},
        onDismiss = {}
    )
}
