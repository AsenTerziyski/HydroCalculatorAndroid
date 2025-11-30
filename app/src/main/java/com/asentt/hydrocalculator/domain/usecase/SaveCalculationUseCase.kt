package com.asentt.hydrocalculator.domain.usecase

import com.asentt.hydrocalculator.data.db.CalculationResultEntity
import com.asentt.hydrocalculator.domain.repo.CalculationRepository
import com.asentt.hydrocalculator.utils.Resource
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