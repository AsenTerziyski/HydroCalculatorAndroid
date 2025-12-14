package com.asentt.hydrocalculator.domain.model

data class ResultData(
    val id: Long = 0,
    val flow: Float,
    val diameter: Float,
    val velocity: Float,
    val headloss: Float,
    val description: String = "N/A"
)