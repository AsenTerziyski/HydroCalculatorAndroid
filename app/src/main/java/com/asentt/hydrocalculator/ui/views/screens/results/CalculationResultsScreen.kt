package com.asentt.hydrocalculator.ui.views.screens.results

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import com.asentt.hydrocalculator.domain.model.ResultData
import com.asentt.hydrocalculator.ui.views.LoadingView
import com.asentt.hydrocalculator.utils.Resource

@Composable
fun CalculationResultsScreen(viewModel: ResultsViewModel = hiltViewModel()) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val results = viewModel
            .resultHistoryState
            .collectAsStateWithLifecycle()
            .value

        when (results) {
            Resource.Loading -> {
                LoadingView()
            }

            is Resource.Success<List<ResultData>> -> {
                val resultList = results.data
                if (resultList.isEmpty()) {
                    Text(text = "No calculation history found.")
                } else {
                    ResultsList(
                        resultList = resultList,
                        onDeleteClick = {
                            viewModel.deleteResultById(it.id)
                        },
                        onShareClick = {}
                    )
                }
            }

            is Resource.Error -> {}

            else -> {}
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
                headloss = 10f,
                velocity = 10f
            ),
            ResultData(
                id = 2,
                flow = 100f,
                diameter = 10f,
                headloss = 10f,
                velocity = 10f
            ),
            ResultData(
                id = 7,
                flow = 100f,
                diameter = 10f,
                headloss = 10f,
                velocity = 10f
            ),
            ResultData(
                id = 9,
                flow = 100f,
                diameter = 10f,
                headloss = 10f,
                velocity = 10f
            ),
            ResultData(
                id = 90,
                flow = 100f,
                diameter = 10f,
                headloss = 10f,
                velocity = 10f
            ),
            ResultData(
                id = 99,
                flow = 100f,
                diameter = 10f,
                headloss = 10f,
                velocity = 10f
            )
        ),
        onDeleteClick = {
        },
        onShareClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun CalculationResultsScreenPreview() {
    CalculationResultsScreen()
}
