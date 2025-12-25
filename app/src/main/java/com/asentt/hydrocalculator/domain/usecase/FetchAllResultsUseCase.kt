package com.asentt.hydrocalculator.domain.usecase

import com.asentt.hydrocalculator.domain.model.ResultData
import com.asentt.hydrocalculator.domain.repo.CalculationRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FetchAllResultsUseCase @Inject constructor(
    private val repo: CalculationRepository
) {
    operator fun invoke() =
        repo.fetchAllResults().map {
            it.mapIndexed { index, entity ->
                ResultData(
                    id = entity.id,
                    flow = entity.flow,
                    diameter = entity.diameter,
                    roughness = entity.roughness,
                    velocity = entity.velocity,
                    headloss = entity.headloss,
                    description = entity.description
                )
            }
        }

}