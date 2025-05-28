package com.example.expensetracker.core.di

import android.app.Application
import androidx.room.Room
import com.example.expensetracker.core.room.database.ExpenseTrackerAppDatabase
import com.example.expensetracker.core.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideExpenseTrackerDatabase(app: Application): ExpenseTrackerAppDatabase {
        return Room.databaseBuilder(
            app,
            ExpenseTrackerAppDatabase::class.java,
            Constants.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}