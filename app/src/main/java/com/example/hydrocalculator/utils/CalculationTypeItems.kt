package com.example.hydrocalculator.utils

data class CalculationTypeItem(
    val title: String,
    val description: String,
)

val calculationTypeItems = listOf<CalculationTypeItem>(
    CalculationTypeItem(
        title = "Pressurized Pipes",
        description = "Estimate flow and velocity in pipes under pressure"
    ),

    CalculationTypeItem(
        title = "Gravity Pipes",
        description = "Estimate flow and velocity in gravity flow pipes"
    ),

    CalculationTypeItem(
        title = "Your results",
        description = "See your calculations"
    )
)