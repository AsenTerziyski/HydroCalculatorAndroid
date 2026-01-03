package com.asentt.hydrocalculator.ui.views.screens.pressure

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asentt.hydrocalculator.ui.theme.HydroCyan

@Composable
fun CatalogSelectionRow(
    selectedPipe: CatalogPipes?,
    selectedPN: PressureRating?,
    isFocused: Boolean = false,
    onFocus: () -> Unit,
    onPipeSelected: (CatalogPipes) -> Unit,
    onPNSelected: (PressureRating) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val borderColor =
        if (isFocused) HydroCyan
        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)

    Row(
        modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .border(2.dp, borderColor, RoundedCornerShape(12.dp))
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Transparent)
                .clickable {
                    onFocus.invoke()
                    expanded = true
                }
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Catalog DN",
                        style = MaterialTheme.typography.bodySmall,
                        color = HydroCyan
                    )
                    selectedPipe?.name?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Select Pipe",
                    tint = HydroCyan
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color.Black)
            ) {
                CatalogPipes.entries.forEach { pipe ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = pipe.name,
                                color = HydroCyan
                            )
                        },
                        onClick = {
                            onPipeSelected(pipe)
                            expanded = false
                        }
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 4.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable {
                        onFocus.invoke()
                        onPNSelected(PressureRating.PN10)
                    }
                    .padding(vertical = 2.dp)
            ) {
                RadioButton(
                    selected = selectedPN == PressureRating.PN10,
                    onClick = null,
                    colors = RadioButtonDefaults.colors(selectedColor = HydroCyan),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "PN10",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(8.dp))
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable {
                        onFocus.invoke()
                        onPNSelected(PressureRating.PN16)
                    }
                    .padding(vertical = 2.dp)
            ) {
                RadioButton(
                    selected = selectedPN == PressureRating.PN16,
                    onClick = null,
                    colors = RadioButtonDefaults.colors(selectedColor = HydroCyan),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "PN16",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun PipeSelectionPreview() {
    CatalogSelectionRow(
        selectedPipe = CatalogPipes.DN32,
        selectedPN = PressureRating.PN10,
        onFocus = {},
        onPipeSelected = {},
        onPNSelected = {}
    )
}


