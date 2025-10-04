package com.example.hydrocalculator.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
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
}

enum class LoadingAppUiState {
    LoadingSplashScreen,
    LoadingWelcomeScreen,
    Ready
}


