package com.asentt.hydrocalculator.ui.views.screens.results

import android.util.Log
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
                                onDeleteClick = {
                                    viewModel.deleteResultById(it.id)
                                },
                                onShareClick = {
                                    Log.d("TAG101", "onShareClick: shared ${it.id}")
                                }
                            )
                        }
                    }
                }
            }

            is Resource.Error -> {}
            else -> {}
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CalculationResultsScreenPreview() {
    CalculationResultsScreen()
}
