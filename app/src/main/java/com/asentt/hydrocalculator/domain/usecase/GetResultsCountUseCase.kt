package com.asentt.hydrocalculator.domain.usecase

import com.asentt.hydrocalculator.domain.repo.CalculationRepository
import javax.inject.Inject

class GetResultsCountUseCase
@Inject constructor(private val repository: CalculationRepository) {
    operator fun invoke() = repository.getResultsCount()
}