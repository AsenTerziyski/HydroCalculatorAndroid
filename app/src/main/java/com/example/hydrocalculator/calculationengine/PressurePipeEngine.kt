package com.example.hydrocalculator.calculationengine

object PressurePipeEngine {
    fun estimateVelocity(flow: Float, diameter: Float): Float =
        (flow / 1000) / getCrossSectionArea(diameter)

    private fun getCrossSectionArea(diameter: Float): Float {
        val diameterInMeters = diameter / 1000
        return Math.PI.toFloat() * (diameterInMeters / 2) * (diameterInMeters / 2)
    }

}