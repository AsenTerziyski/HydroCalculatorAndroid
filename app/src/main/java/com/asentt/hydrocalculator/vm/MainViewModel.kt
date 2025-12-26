package com.asentt.hydrocalculator.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asentt.hydrocalculator.domain.usecase.GetResultsCountUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(getResultsCountUseCase: GetResultsCountUseCase) :
    ViewModel() {
    private val _startupState = MutableStateFlow(LoadingAppUiState.LoadingSplashScreen)
    val startupState = _startupState.asStateFlow()


    init {
        viewModelScope.launch {
            delay(1000)
            _startupState.value = LoadingAppUiState.LoadingWelcomeScreen
            delay(2000)
            _startupState.value = LoadingAppUiState.Ready
        }
    }

    val resultsBadgeCount = getResultsCountUseCase.invoke().stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )
}

enum class LoadingAppUiState {
    LoadingSplashScreen,
    LoadingWelcomeScreen,
    Ready
}
