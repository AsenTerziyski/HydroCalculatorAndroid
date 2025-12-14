package com.asentt.hydrocalculator.ui.views.screens.results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asentt.hydrocalculator.data.db.CalculationResultEntity
import com.asentt.hydrocalculator.domain.usecase.FetchAllResultsUseCase
import com.asentt.hydrocalculator.domain.usecase.ResultData
import com.asentt.hydrocalculator.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class ResultsViewModel @Inject constructor(
    private val fetchAllResultsUseCase: FetchAllResultsUseCase
) : ViewModel() {

    private val _resultHistoryState =
        MutableStateFlow<Resource<List<ResultData>>>(Resource.Idle)
    val resultHistoryState = _resultHistoryState.asStateFlow()

    init {
        fetchAllResults()
    }

    private fun fetchAllResults() =
        fetchAllResultsUseCase()
            .onStart {
                _resultHistoryState.value = Resource.Loading
                delay(1000)
            }
            .onEach { results ->
                delay(1000)
                _resultHistoryState.update {
                    Resource.Success(results)
                }
            }
            .catch { e ->
                delay(1000)
                _resultHistoryState.update {
                    Resource.Error(e as? Exception ?: Exception(e))
                }
            }
            .launchIn(viewModelScope)


}