package com.example.hydrocalculator.di

import android.content.Context
import androidx.room.Room
import com.example.hydrocalculator.data.db.AppDatabase
import com.example.hydrocalculator.data.db.PressureCalculationResultsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "hydro_calculator_db" // actual file name of the database on the device.
        ).build()
    }

    @Provides
    // No need for @Singleton here, as Hilt provides the same DAO instance
    // from the singleton AppDatabase instance.
    fun provideCalculationResultDao(appDatabase: AppDatabase): PressureCalculationResultsDao {
        return appDatabase.calculationResultsDao()
    }
}