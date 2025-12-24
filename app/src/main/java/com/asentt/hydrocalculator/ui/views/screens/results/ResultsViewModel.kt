package com.asentt.hydrocalculator.ui.views.screens.results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asentt.hydrocalculator.data.db.CalculationResultEntity
import com.asentt.hydrocalculator.domain.usecase.FetchAllResultsUseCase
import com.asentt.hydrocalculator.domain.model.ResultData
import com.asentt.hydrocalculator.domain.usecase.DeleteResultByIdUseCase
import com.asentt.hydrocalculator.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ResultsViewModel @Inject constructor(
    private val fetchAllResultsUseCase: FetchAllResultsUseCase,
    private val deleteResultByIdUseCase: DeleteResultByIdUseCase
) : ViewModel() {

    private val _resultHistoryState =
        MutableStateFlow<Resource<List<ResultData>>>(Resource.Idle)
    val resultHistoryState = _resultHistoryState.asStateFlow()

    init {
        fetchAllResults()
    }

    fun deleteResultById(resultId: Long) {
        viewModelScope.launch {
            _resultHistoryState.update { Resource.Loading }
            try {
                deleteResultByIdUseCase.invoke(resultId)
            } catch (e: Exception) {
                _resultHistoryState.update {
                    Resource.Error(e)
                }
            }
        }
    }

    private fun fetchAllResults() =
        fetchAllResultsUseCase()
            .onStart {
                _resultHistoryState.value = Resource.Loading
            }
            .onEach { results ->
                _resultHistoryState.update {
                    Resource.Success(results)
                }
            }
            .catch { e ->
                _resultHistoryState.update {
                    Resource.Error(e as? Exception ?: Exception(e))
                }
            }
            .launchIn(viewModelScope)


    private val _sortOption = MutableStateFlow(SortOption.NEWEST)
    val sortOption = _sortOption.asStateFlow()

    val sortedResultState = combine(
        fetchAllResultsUseCase.invoke(),
        _sortOption
    ) { resultResource, sortOption ->
        val sortedList = when (sortOption) {
            SortOption.NEWEST -> resultResource.sortedByDescending { it.id }
            SortOption.FLOW -> resultResource.sortedByDescending { it.flow }
            SortOption.DIAMETER -> resultResource.sortedByDescending { it.diameter }
            SortOption.VELOCITY -> resultResource.sortedByDescending { it.velocity }
            SortOption.HEADLOSSES -> resultResource.sortedByDescending { it.headloss }
        }
        Resource.Success(sortedList) as Resource<List<ResultData>>
    }.onStart {
        emit(Resource.Loading)
    }.catch { e ->
        emit(Resource.Error(e as? Exception ?: Exception(e)))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Resource.Loading
    )
    fun onSortOptionSelected(newOption: SortOption) {
        _sortOption.update { _ -> newOption }
    }

}