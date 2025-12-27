package com.asentt.hydrocalculator.ui.views.snackbar
sealed interface SnackBarEvent {
    data class ShowSnackBar(val message: String) : SnackBarEvent

}