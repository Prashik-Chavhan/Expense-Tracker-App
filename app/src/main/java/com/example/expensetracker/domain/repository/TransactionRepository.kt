package com.example.expensetracker.domain.repository

import com.example.expensetracker.core.room.entities.TransactionEntity
import com.example.expensetracker.domain.models.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {

    suspend fun createTransaction(transaction: Transaction)

    fun getAllTransactions(): Flow<List<TransactionEntity>>

    suspend fun getTransactionById(transactionId:String):TransactionEntity?

    fun getTransactionsForACertainDay(date:String):Flow<List<TransactionEntity>>

    fun getTransactionsBetweenTwoDates(dates:List<String>):Flow<List<TransactionEntity>>

    fun searchTransactions(startDate: String, endDate: String,categoryId:String): Flow<List<TransactionEntity>>

    suspend fun deleteTransactionById(transactionId:String)

    fun getTransactionByCategory(categoryId: String): Flow<List<TransactionEntity>>
}