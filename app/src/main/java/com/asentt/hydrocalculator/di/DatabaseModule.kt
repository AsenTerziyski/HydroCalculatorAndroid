package com.asentt.hydrocalculator.di

import android.content.Context
import androidx.room.Room
import com.asentt.hydrocalculator.data.db.AppDatabase
import com.asentt.hydrocalculator.data.db.PressureCalculationResultsDao
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
            "hydro_calculator_db"
        ).build()
    }

    @Provides
    fun provideCalculationResultDao(appDatabase: AppDatabase): PressureCalculationResultsDao {
        return appDatabase.calculationResultsDao()
    }
}