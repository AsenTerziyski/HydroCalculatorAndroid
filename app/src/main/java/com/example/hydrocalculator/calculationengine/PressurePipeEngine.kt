package com.example.hydrocalculator.calculationengine

object PressurePipeEngine {
    fun estimateVelocity(flow: Float, diameter: Float): Float =
        (flow / 1000) / (diameter / 1000)
}