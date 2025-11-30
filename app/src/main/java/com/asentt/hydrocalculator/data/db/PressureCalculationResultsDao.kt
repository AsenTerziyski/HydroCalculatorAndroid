package com.asentt.hydrocalculator.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PressureCalculationResultsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(calculation: CalculationResultEntity)

    @Query("SELECT * FROM pressure_calculation_results ORDER BY timestamp DESC")
    fun getAllResults(): Flow<List<CalculationResultEntity>>

    @Query("DELETE FROM pressure_calculation_results WHERE id = :id")
    suspend fun deleteById(id: Long)

}