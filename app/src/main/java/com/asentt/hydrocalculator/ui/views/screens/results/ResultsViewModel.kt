package com.asentt.hydrocalculator.ui.views.screens.results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asentt.hydrocalculator.domain.usecase.FetchAllResultsUseCase
import com.asentt.hydrocalculator.domain.model.ResultData
import com.asentt.hydrocalculator.domain.usecase.DeleteResultByIdUseCase
import com.asentt.hydrocalculator.ui.views.snackbar.SnackBarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ResultsViewModel @Inject constructor(
    fetchAllResultsUseCase: FetchAllResultsUseCase,
    private val deleteResultByIdUseCase: DeleteResultByIdUseCase
) : ViewModel() {
    private val _snackBarEventChannel = Channel<SnackBarEvent>()
    val snackBarEventChannel = _snackBarEventChannel.receiveAsFlow()

    private val _sortOption = MutableStateFlow(SortOption.Newest)
    private val _isDeleting = MutableStateFlow(false)

    val uiState = combine(
        flow = fetchAllResultsUseCase.invoke(),
        flow2 = _sortOption,
        flow3 = _isDeleting
    ) { results,
        sortOption,
        isDeleting ->

        val sortedList = when (sortOption) {
            SortOption.Newest -> results.sortedByDescending { it.id }
            SortOption.Flow -> results.sortedByDescending { it.flow }
            SortOption.Diameter -> results.sortedByDescending { it.diameter }
            SortOption.Roughness -> results.sortedByDescending { it.roughness }
            SortOption.Velocity -> results.sortedByDescending { it.velocity }
            SortOption.Headlosses -> results.sortedByDescending { it.headloss }
        }

        ResultsUiState(
            results = sortedList,
            sortOption = sortOption,
            isLoading = isDeleting,
            error = null
        )

    }.catch { emit(ResultsUiState(error = it.message))
    }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ResultsUiState(isLoading = true)
        )

    fun deleteResultById(resultId: Long) {
        viewModelScope.launch {
            _isDeleting.update { true }
            try {
                deleteResultByIdUseCase.invoke(resultId)
                _snackBarEventChannel.send(SnackBarEvent.ShowSnackBar("Deleted successfully"))
            } catch (e: Exception) {
                _snackBarEventChannel.send(SnackBarEvent.ShowSnackBar("Error: ${e.message}"))
            } finally {
                _isDeleting.update { false }
            }
        }
    }

    fun onSortOptionSelected(newOption: SortOption) {
        _sortOption.update { _ -> newOption }
    }

}

data class ResultsUiState(
    val results: List<ResultData> = emptyList(),
    val sortOption: SortOption = SortOption.Newest,
    val isLoading: Boolean = false,
    val error: String? = null
)