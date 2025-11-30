package com.example.hydrocalculator.domain.usecase

import com.example.hydrocalculator.data.db.CalculationResultEntity
import com.example.hydrocalculator.domain.repo.CalculationRepository
import com.example.hydrocalculator.utils.Resource
import javax.inject.Inject
import kotlin.String

class SaveCalculationUseCase @Inject constructor(private val calculationRepository: CalculationRepository) {

    suspend operator fun invoke(
        waterFlow: Float,
        pipeDiameter: Float,
        velocity: Float,
        headLoss: Float,
        description: String
    ): Resource<Unit> = calculationRepository.saveCalculation(
        CalculationResultEntity(
            flow = waterFlow,
            diameter = pipeDiameter,
            velocity = velocity,
            headloss = headLoss,
            description = description.ifEmpty { "N/A" }
        )
    )
}