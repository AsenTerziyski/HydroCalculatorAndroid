package com.example.hydrocalculator.navigation

sealed class HydroAppRoutes(val route: String) {
    object Welcome: HydroAppRoutes("welcome_screen")
    object Main: HydroAppRoutes("Main_screen")
}