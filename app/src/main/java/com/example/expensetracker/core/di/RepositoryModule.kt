package com.example.expensetracker.core.di

import com.example.expensetracker.core.room.database.ExpenseTrackerAppDatabase
import com.example.expensetracker.data.ExpenseCategoryRepositoryImpl
import com.example.expensetracker.data.ExpenseRepositoryImpl
import com.example.expensetracker.data.IncomeRepositoryImpl
import com.example.expensetracker.data.TransactionCategoryRepositoryImpl
import com.example.expensetracker.data.TransactionRepositoryImpl
import com.example.expensetracker.domain.repository.ExpenseCategoryRepository
import com.example.expensetracker.domain.repository.ExpenseRepository
import com.example.expensetracker.domain.repository.IncomeRepository
import com.example.expensetracker.domain.repository.TransactionCategoryRepository
import com.example.expensetracker.domain.repository.TransactionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideExpenseCategoryRepository(
        database: ExpenseTrackerAppDatabase,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): ExpenseCategoryRepository {
        return ExpenseCategoryRepositoryImpl(
            db = database,
            ioDispatcher = ioDispatcher
        )
    }

    @Provides
    @Singleton
    fun provideExpenseRepository(
        database: ExpenseTrackerAppDatabase,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): ExpenseRepository {
        return ExpenseRepositoryImpl(
            db = database,
            ioDispatcher = ioDispatcher
        )
    }

    @Provides
    @Singleton
    fun provideTransactionCategoryRepository(
        database: ExpenseTrackerAppDatabase,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): TransactionCategoryRepository {
        return TransactionCategoryRepositoryImpl(
            db = database,
            ioDispatcher = ioDispatcher
        )
    }

    @Provides
    @Singleton
    fun provideTransactionRepository(
        database: ExpenseTrackerAppDatabase,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): TransactionRepository {
        return TransactionRepositoryImpl(
            db = database,
            ioDispatcher = ioDispatcher,
        )
    }

    @Provides
    @Singleton
    fun provideIncomeRepository(
        database: ExpenseTrackerAppDatabase,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
    ): IncomeRepository {
        return IncomeRepositoryImpl(
            db = database,
            ioDispatcher = ioDispatcher,
        )
    }
}