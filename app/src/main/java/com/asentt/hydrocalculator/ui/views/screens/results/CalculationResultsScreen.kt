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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.asentt.hydrocalculator.domain.model.ResultData
import com.asentt.hydrocalculator.ui.views.snackbar.SnackBarToastView
import com.asentt.hydrocalculator.ui.views.LoadingView
import com.asentt.hydrocalculator.ui.views.dialogs.ConfirmationDialog
import com.asentt.hydrocalculator.ui.views.snackbar.SnackBarEvent
import com.asentt.hydrocalculator.utils.Resource
import kotlinx.coroutines.launch

@Composable
fun CalculationResultsScreen(viewModel: ResultsViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var itemToDelete by remember { mutableStateOf<ResultData?>(null) }

    LaunchedEffect(key1 = Unit) {
        viewModel.snackBarEventChannel.collect { snackBarEvent ->
            when (snackBarEvent) {
                is SnackBarEvent.ShowSnackBar -> {
                    scope.launch {
                        snackBarHostState.showSnackbar(
                            message = snackBarEvent.message,
                            withDismissAction = true
                        )
                    }
                }
            }
        }
    }

    if (itemToDelete != null) {
        ConfirmationDialog(
            dialogTitle = "Delete result",
            dialogText = "Are you sure you want to delete this result?",
            confirmButtonText = "Delete",
            dismissButtonText = "Cancel",
            hasIcon = false,
            onDismissRequest = { itemToDelete = null },
            onConfirmation = {
                itemToDelete?.let { viewModel.deleteResultById(it.id) }
                itemToDelete = null
            }
        )
    }

    CalculationResultsScreen(
        results = uiState.results,
        isLoading = uiState.isLoading,
        error = uiState.error,
        currentSortOption = uiState.sortOption,
        snackBarHostState = snackBarHostState,
        onSortSelected = viewModel::onSortOptionSelected,
        onDeleteClick = { result -> itemToDelete = result },
        onShareClick = { /* Handle share */ }
    )
}

@Composable
private fun CalculationResultsScreen(
    results: List<ResultData>,
    isLoading: Boolean,
    error: String?,
    currentSortOption: SortOption,
    snackBarHostState: SnackbarHostState,
    onSortSelected: (SortOption) -> Unit,
    onDeleteClick: (ResultData) -> Unit,
    onShareClick: (ResultData) -> Unit
) {
    Scaffold(
        snackbarHost = { SnackBarToastView(snackBarHostState) }
    ) { paddingValues ->

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

                when {
                    isLoading -> LoadingView()
                    error != null -> Text("Error: $error")
                    results.isEmpty() -> Text(text = "No calculation history found.")
                    else -> {
                        ResultsList(
                            resultList = results,
                            onDeleteClick = onDeleteClick,
                            onShareClick = onShareClick
                        )
                    }
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
            items = resultList,
            key = { result -> result.id }
        ) { result ->
            ResultItem(
                calculationResult = result,
                onDeleteClick = { onDeleteClick(result) },
                onShareClick = { onShareClick(result) }
            )
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
