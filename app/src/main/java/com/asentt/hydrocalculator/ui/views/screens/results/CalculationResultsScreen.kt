package com.asentt.hydrocalculator.ui.views.screens.results

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.asentt.hydrocalculator.data.db.CalculationResultEntity
import com.asentt.hydrocalculator.domain.usecase.ResultData
import com.asentt.hydrocalculator.ui.views.LoadingView
import com.asentt.hydrocalculator.utils.Resource

@Composable
fun CalculationResultsScreen(
    viewModel: ResultsViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val resultState = viewModel.resultHistoryState.collectAsStateWithLifecycle()
        val results = resultState.value

        when(results) {
            Resource.Loading -> {
                LoadingView()
            }
            is Resource.Error -> {

            }
            is Resource.Success<*> -> {
                val results = results.data as? List<*>
                if (results?.isEmpty() == true) {

                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(
                            count = results?.size as Int,
                            key = { result -> (result as? Any)?.hashCode() ?: 0 }
                        ) { result ->
                            val test = results[result]
                            ResultItem(test as ResultData)
                        }
                    }
                }
            }
            else -> {}
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CalculationResultsScreenPreview() {
    CalculationResultsScreen()
}
