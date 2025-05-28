package com.example.expensetracker.data

import com.example.expensetracker.core.di.IoDispatcher
import com.example.expensetracker.core.room.database.ExpenseTrackerAppDatabase
import com.example.expensetracker.core.room.entities.TransactionEntity
import com.example.expensetracker.domain.models.Transaction
import com.example.expensetracker.domain.repository.TransactionRepository
import com.example.expensetracker.domain.Mappers.toEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val db: ExpenseTrackerAppDatabase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : TransactionRepository {
    override suspend fun createTransaction(transaction: Transaction) {
        return withContext(ioDispatcher) {
            db.transactionEntityDao.insertTransaction(
                transactionEntity = transaction.toEntity()
            )
        }
    }

    override suspend fun getTransactionById(transactionId: String): TransactionEntity? {
        return withContext(ioDispatcher) {
            db.transactionEntityDao.getTransactionById(id = transactionId)
        }
    }

    override fun getAllTransactions(): Flow<List<TransactionEntity>> {
        return db.transactionEntityDao.getTransactions().flowOn(ioDispatcher)

    }

    override fun getTransactionsForACertainDay(date: String): Flow<List<TransactionEntity>> {
        return db.transactionEntityDao.getTransactionsForACertainDay(date = date)
            .flowOn(ioDispatcher)
    }

    override fun getTransactionsBetweenTwoDates(dates: List<String>): Flow<List<TransactionEntity>> {
        return db.transactionEntityDao.getTransactionsBetweenTwoDates(dates = dates)
            .flowOn(ioDispatcher)
    }

    override fun searchTransactions(
//        dates: List<String>,
        startDate: String, endDate: String,
        categoryId: String
    ): Flow<List<TransactionEntity>> {
        return db.transactionEntityDao.searchTransactions(startDate, endDate, categoryId = categoryId)
            .flowOn(ioDispatcher)
    }

    override suspend fun deleteTransactionById(transactionId: String) {
        withContext(ioDispatcher){
            db.transactionEntityDao.deleteTransactionById(id = transactionId)
        }
    }

    override fun getTransactionByCategory(categoryId: String): Flow<List<TransactionEntity>> {
        return db.transactionEntityDao.getTransactionsByCategory(categoryId = categoryId)
            .flowOn(ioDispatcher)
    }

}