package com.asentt.hydrocalculator.ui.views.screens.results

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.runtime.getValue
import com.asentt.hydrocalculator.domain.model.ResultData
import com.asentt.hydrocalculator.ui.views.LoadingView
import com.asentt.hydrocalculator.utils.Resource

@Composable
fun CalculationResultsScreen(viewModel: ResultsViewModel = hiltViewModel()) {
    val resultsState by viewModel.sortedResultState.collectAsStateWithLifecycle()
    val currentSortOption by viewModel.sortOption.collectAsStateWithLifecycle()

    CalculationResultsScreen(
        resultsState = resultsState,
        currentSortOption = currentSortOption,
        onSortSelected = viewModel::onSortOptionSelected,
        onDeleteClick = { viewModel.deleteResultById(it.id) },
        onShareClick = { /* Handle share */ }
    )

}

@Composable
private fun CalculationResultsScreen(
    resultsState: Resource<List<ResultData>>,
    currentSortOption: SortOption,
    onSortSelected: (SortOption) -> Unit,
    onDeleteClick: (ResultData) -> Unit,
    onShareClick: (ResultData) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SortOption.entries.forEach { sortOption ->
                FilterChip(
                    selected = (sortOption == currentSortOption),
                    onClick = {
                        onSortSelected.invoke(sortOption)
                    },
                    label = {
                        Text(
                            text = when (sortOption) {
                                SortOption.NEWEST -> SortOption.NEWEST.name
                                SortOption.FLOW -> SortOption.FLOW.name
                                SortOption.DIAMETER -> SortOption.DIAMETER.name
                                SortOption.VELOCITY -> SortOption.VELOCITY.name
                                SortOption.HEADLOSSES -> SortOption.HEADLOSSES.name
                            }
                        )
                    }
                )
            }
        }

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            when (resultsState) {
                is Resource.Loading -> LoadingView()
                is Resource.Success -> {
                    val resultList = resultsState.data
                    if (resultList.isEmpty()) {
                        Text(text = "No calculation history found.")
                    } else {
                        ResultsList(
                            resultList = resultList,
                            onDeleteClick = onDeleteClick,
                            onShareClick = onShareClick
                        )
                    }
                }
                is Resource.Error -> Text("Error loading results")
                else -> {}
            }
        }
    }
}

@Composable
fun ResultsList(
    resultList: List<ResultData>,
    onDeleteClick: (ResultData) -> Unit,
    onShareClick: (ResultData) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(
            items = resultList, key = { result -> result.id }) { result ->
            ResultItem(
                calculationResult = result,
                onDeleteClick = { onDeleteClick(result) },
                onShareClick = { onShareClick(result) })
        }
    }
}

@Preview
@Composable
fun ResultsListPreview() {
    ResultsList(
        resultList = listOf(
            ResultData(
                id = 1, flow = 100f, diameter = 10f, headloss = 10f, velocity = 10f
            ), ResultData(
                id = 2, flow = 100f, diameter = 10f, headloss = 10f, velocity = 10f
            ), ResultData(
                id = 7, flow = 100f, diameter = 10f, headloss = 10f, velocity = 10f
            ), ResultData(
                id = 9, flow = 100f, diameter = 10f, headloss = 10f, velocity = 10f
            ), ResultData(
                id = 90, flow = 100f, diameter = 10f, headloss = 10f, velocity = 10f
            ), ResultData(
                id = 99, flow = 100f, diameter = 10f, headloss = 10f, velocity = 10f
            )
        ), onDeleteClick = {}, onShareClick = {})
}
