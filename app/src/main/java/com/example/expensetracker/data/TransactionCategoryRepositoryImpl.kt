package com.example.expensetracker.data

import com.example.expensetracker.core.di.IoDispatcher
import com.example.expensetracker.core.room.database.ExpenseTrackerAppDatabase
import com.example.expensetracker.core.room.entities.TransactionCategoryEntity
import com.example.expensetracker.domain.models.TransactionCategory
import com.example.expensetracker.domain.repository.TransactionCategoryRepository
import com.example.expensetracker.domain.Mappers.toEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TransactionCategoryRepositoryImpl @Inject constructor(
    private val db:ExpenseTrackerAppDatabase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) :TransactionCategoryRepository{
    override suspend fun saveTransactionCategory(transactionCategory: TransactionCategory) {
        withContext(ioDispatcher){
            db.transactionCategoryEntityDao.insertTransactionCategory(
                transactionCategoryEntity = transactionCategory.toEntity())
        }
    }

    override suspend fun getTransactionCategoryById(transactionId: String): TransactionCategoryEntity? {
        return withContext(ioDispatcher){
            db.transactionCategoryEntityDao.getTransactionCategoryById(id = transactionId)
        }
    }

    override fun getAllTransactionCategories(): Flow<List<TransactionCategoryEntity>> {
        return db.transactionCategoryEntityDao.getTransactionCategories().flowOn(ioDispatcher)
    }

    override suspend fun getTransactionCategoryByName(name: String): TransactionCategoryEntity? {
        return withContext(ioDispatcher){
            db.transactionCategoryEntityDao.getTransactionCategoryByName(name = name)
        }
    }

    override suspend fun updateTransactionCategory(
        transactionCategoryId: String,
        transactionCategoryName: String
    ) {
        withContext(ioDispatcher){
            db.transactionCategoryEntityDao.updateExpenseCategoryName(
                id = transactionCategoryId,
                name = transactionCategoryName,
            )
        }

    }

    override suspend fun deleteTransactionCategory(transactionCategoryId: String) {
        withContext(ioDispatcher){
            db.transactionCategoryEntityDao.deleteTransactionCategoryById(
                id = transactionCategoryId)
        }
    }
}