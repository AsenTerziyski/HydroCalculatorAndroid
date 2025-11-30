package com.asentt.hydrocalculator.navigation

sealed class HydroAppRoutes(val route: String) {
    object WelcomeScreen : HydroAppRoutes(WELCOME_ROUTE)
    object HomeScreen : HydroAppRoutes(HOME_ROUTE)
    object GoodbyeScreen : HydroAppRoutes(GOODBYE_ROUTE)
    object PressureScreen : HydroAppRoutes(PRESSURE_CALCULATION_SCREEN)

    private companion object {
        const val WELCOME_ROUTE = "welcome_screen"
        const val HOME_ROUTE = "home_screen"
        const val GOODBYE_ROUTE = "goodbye_screen"
        const val PRESSURE_CALCULATION_SCREEN = "pressure_screen"
    }
}
