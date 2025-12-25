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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.asentt.hydrocalculator.domain.model.ResultData
import com.asentt.hydrocalculator.ui.views.LoadingView
import com.asentt.hydrocalculator.ui.views.screens.pressure.CalculationPressureEvent
import com.asentt.hydrocalculator.utils.Resource
import kotlinx.coroutines.launch

@Composable
fun CalculationResultsScreen(viewModel: ResultsViewModel = hiltViewModel()) {
    val resultsState by viewModel.sortedResultState.collectAsStateWithLifecycle()
    val currentSortOption by viewModel.sortOption.collectAsStateWithLifecycle()

    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        viewModel.eventChannel.collect {
            when (it) {
                CalculationPressureEvent.HideSaveDialog -> {}
                CalculationPressureEvent.ShowSaveDialog -> {}
                is CalculationPressureEvent.ShowSnackBar -> {
                    scope.launch {
                        snackBarHostState.showSnackbar(
                            message = it.message,
                            withDismissAction = true
                        )
                    }
                }
            }
        }
    }

    CalculationResultsScreen(
        resultsState = resultsState,
        currentSortOption = currentSortOption,
        snackBarHostState = snackBarHostState,
        onSortSelected = viewModel::onSortOptionSelected,
        onDeleteClick = { viewModel.deleteResultById(it.id) },
        onShareClick = { /* Handle share */ }
    )

}

@Composable
private fun CalculationResultsScreen(
    resultsState: Resource<List<ResultData>>,
    currentSortOption: SortOption,
    snackBarHostState: SnackbarHostState,
    onSortSelected: (SortOption) -> Unit,
    onDeleteClick: (ResultData) -> Unit,
    onShareClick: (ResultData) -> Unit
) {
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) })
    { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

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
                                    SortOption.Newest -> SortOption.Newest.name
                                    SortOption.Flow -> SortOption.Flow.name
                                    SortOption.Diameter -> SortOption.Diameter.name
                                    SortOption.Roughness -> SortOption.Roughness.name
                                    SortOption.Velocity -> SortOption.Velocity.name
                                    SortOption.Headlosses -> SortOption.Headlosses.name
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
                id = 1,
                flow = 100f,
                diameter = 10f,
                roughness = 0.01F,
                headloss = 10f,
                velocity = 10f
            ), ResultData(
                id = 2,
                flow = 100f,
                diameter = 10f,
                roughness = 0.01F,
                headloss = 10f,
                velocity = 10f
            ), ResultData(
                id = 7,
                flow = 100f,
                diameter = 10f,
                roughness = 0.01F,
                headloss = 10f,
                velocity = 10f
            ), ResultData(
                id = 9,
                flow = 100f,
                diameter = 10f,
                roughness = 0.01F,
                headloss = 10f,
                velocity = 10f
            ), ResultData(
                id = 90,
                flow = 100f,
                diameter = 10f,
                roughness = 0.01F,
                headloss = 10f,
                velocity = 10f
            ), ResultData(
                id = 99,
                flow = 100f,
                diameter = 10f,
                roughness = 0.01F,
                headloss = 10f,
                velocity = 10f
            )
        ), onDeleteClick = {}, onShareClick = {})
}
