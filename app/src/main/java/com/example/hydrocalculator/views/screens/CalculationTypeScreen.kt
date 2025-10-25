package com.example.hydrocalculator.views.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hydrocalculator.utils.CalculationTypeItem
import com.example.hydrocalculator.utils.calculationTypeItems
import com.example.hydrocalculator.views.calculationtype.CalculationTypeCard

@Composable
fun CalculationTypeScreen(onCardClick: (CalculationTypeItem) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(count = calculationTypeItems.size) {
                val item = calculationTypeItems[it]
                CalculationTypeCard(
                    title = calculationTypeItems[it].title,
                    description = calculationTypeItems[it].description
                ) {
                    onCardClick.invoke(item)
                }
            }
        }
    }
}