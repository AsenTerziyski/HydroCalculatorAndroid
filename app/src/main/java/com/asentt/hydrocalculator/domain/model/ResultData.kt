package com.asentt.hydrocalculator.domain.model

data class ResultData(
    val id: Long,
    val flow: Float,
    val diameter: Float,
    val roughness: Float,
    val velocity: Float,
    val headloss: Float,
    val description: String = "N/A"
)