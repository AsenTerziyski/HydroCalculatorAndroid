package com.asentt.hydrocalculator.ui.views.screens.results

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asentt.hydrocalculator.domain.model.ResultData

@Composable
fun ResultItem(
    calculationResult: ResultData,
    onShareClick: (ResultData) -> Unit = {},
    onDeleteClick: (ResultData) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {

        Column(
            modifier = Modifier.animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        ) {

            HeaderResultRow(
                calculationResult = calculationResult,
                isExpanded = expanded,
                onShareClick = onShareClick,
                onDeleteClick = onDeleteClick,
                onExpandClick = { expanded = !expanded }
            )

            ResultRow(
                firstText = "Flow: ",
                secondText = "%.2f".format(calculationResult.flow),
                thirdText = "l/s"
            )

            if (expanded) {
                ResultRow(
                    firstText = "Roughness: ",
                    secondText = "%.4f".format(calculationResult.roughness),
                    thirdText = "mm"
                )
                ResultRow(
                    firstText = "Velocity: ",
                    secondText = "%.2f".format(calculationResult.velocity),
                    thirdText = "m/s"
                )
                ResultRow(
                    firstText = "Description: ",
                    secondText = calculationResult.description,
                    thirdText = ""
                )
                Spacer(modifier = Modifier.padding(8.dp))
            } else {
                Spacer(modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@Composable
fun HeaderResultRow(
    calculationResult: ResultData,
    isExpanded: Boolean = false,
    onShareClick: (ResultData) -> Unit,
    onDeleteClick: (ResultData) -> Unit,
    onExpandClick: () -> Unit = {}
) {
    val rotationState by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Absolute.SpaceBetween
    ) {
        ResultRow(
            modifier = Modifier.weight(1f),
            firstText = "Diameter: ",
            secondText = "%.2f".format(calculationResult.diameter),
            thirdText = "mm"
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { onShareClick(calculationResult) }) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share Result",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = { onDeleteClick(calculationResult) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete Result",
                    tint = MaterialTheme.colorScheme.error
                )
            }
            IconButton(onClick = onExpandClick) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    modifier = Modifier.rotate(rotationState)
                )
            }
        }
    }
}

@Composable
fun ResultRow(
    modifier: Modifier = Modifier,
    firstText: String,
    secondText: String,
    thirdText: String,
) {
    Row(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = firstText,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(0.dp)
        )
        Text(
            text = secondText,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(0.dp)
        )
        Text(
            text = thirdText,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(0.dp)
        )
    }
}

@Preview
@Composable
fun ResultItemPreview() {
    ResultItem(
        calculationResult = getPreviewResult()
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F0F0)
@Composable
fun HeaderResultRowNotExpandedPreview() {
    HeaderResultRow(
        calculationResult = getPreviewResult(),
        isExpanded = false,
        {},
        {}
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F0F0)
@Composable
fun HeaderResultRowExpandedPreview() {
    HeaderResultRow(
        calculationResult = getPreviewResult(),
        isExpanded = true,
        {},
        {}
    )
}

private fun getPreviewResult() = ResultData(
    id = 100L,
    flow = 100F,
    diameter = 100.10F,
    roughness = 0.00001F,
    velocity = 1.1F,
    headloss = 0.001F,
    description = "N/A"
)