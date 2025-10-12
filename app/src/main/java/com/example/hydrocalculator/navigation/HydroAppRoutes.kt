package com.example.hydrocalculator.navigation

sealed class HydroAppRoutes(val route: String) {
    object Welcome : HydroAppRoutes(WELCOME_ROUTE)
    object Main : HydroAppRoutes(MAIN_ROUTE)

    object Goodbye: HydroAppRoutes(GOODBYE_ROUTE)

    private companion object {
        const val WELCOME_ROUTE = "welcome_screen"
        const val MAIN_ROUTE = "main_screen"

        const val GOODBYE_ROUTE = "goodbye_screen"
    }
}
