package com.asentt.hydrocalculator.domain.repo

import com.asentt.hydrocalculator.data.db.CalculationResultEntity
import com.asentt.hydrocalculator.data.db.PressureCalculationResultsDao
import com.asentt.hydrocalculator.utils.Resource
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CalculationRepository @Inject constructor(private val calculationDao: PressureCalculationResultsDao) {
    suspend fun saveCalculation(calculation: CalculationResultEntity): Resource<Unit> = try {
        delay(500)
        calculationDao.insert(calculation)
        Resource.Success(Unit)
    } catch (e: Exception) {
        delay(500)
        Resource.Error(e)
    }

}