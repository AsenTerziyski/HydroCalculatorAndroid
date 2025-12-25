package com.asentt.hydrocalculator.calculationengine

import javax.inject.Inject
import kotlin.math.pow

class PressurePipeEngine @Inject constructor() {
    fun estimateVelocity(flow: Float, diameter: Float, roughness: Float): Float =
        (roughness * flow / 1000) / getCrossSectionArea(diameter) // dummy formula

    fun estimateHeadloss(flow: Float, diameter: Float, roughness: Float): Float =
        100 * flow * roughness / diameter // dummy formula


    private fun getCrossSectionArea(diameter: Float): Float {
        val diameterInMeters = diameter / 1000
        return Math.PI.toFloat() * (diameterInMeters / 2).pow(2)
    }
}