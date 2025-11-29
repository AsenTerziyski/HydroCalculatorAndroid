package com.example.hydrocalculator.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pressure_calculation_results")
data class CalculationResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val description: String,
    val flow: Float,
    val diameter: Float,
    val velocity: Float,
    val headloss: Float,
    val timestamp: Long = System.currentTimeMillis()
)