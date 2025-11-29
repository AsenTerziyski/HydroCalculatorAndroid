package com.example.hydrocalculator.calculationengine

import javax.inject.Inject
import kotlin.math.pow

class PressurePipeEngine @Inject constructor() {
    fun estimateVelocity(flow: Float, diameter: Float): Float =
        (flow / 1000) / getCrossSectionArea(diameter)

    fun estimateHeadloss(flow: Float, diameter: Float): Float =
        100 * flow / diameter // dummy formula


    private fun getCrossSectionArea(diameter: Float): Float {
        val diameterInMeters = diameter / 1000
        return Math.PI.toFloat() * (diameterInMeters / 2).pow(2)
    }
}