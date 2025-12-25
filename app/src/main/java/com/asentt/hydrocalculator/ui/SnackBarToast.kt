package com.asentt.hydrocalculator.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.asentt.hydrocalculator.ui.theme.HydroCyan

@Composable
fun SnackBarToastView(snackBarHostState: SnackbarHostState) {
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
}