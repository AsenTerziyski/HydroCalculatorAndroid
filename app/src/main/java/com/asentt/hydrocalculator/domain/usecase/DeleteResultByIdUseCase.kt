package com.asentt.hydrocalculator.domain.usecase

import android.util.Log
import com.asentt.hydrocalculator.domain.repo.CalculationRepository
import javax.inject.Inject

class DeleteResultByIdUseCase @Inject constructor(
    private val repository: CalculationRepository
) {
    suspend operator fun invoke(resultId: Long) {
        Log.d("TAG101", "deleteResultById: $resultId")
        repository.deleteCalculation(resultId)
    }
}