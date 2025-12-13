package com.asentt.hydrocalculator.domain.usecase

import com.asentt.hydrocalculator.domain.repo.CalculationRepository
import javax.inject.Inject

class FetchAllResultsUseCase @Inject constructor(
    private val repo: CalculationRepository
) {
    operator fun invoke() = repo.fetchAllResults()

}