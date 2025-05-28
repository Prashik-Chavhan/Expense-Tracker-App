package com.example.expensetracker.domain.repository

import com.example.expensetracker.core.room.entities.TransactionCategoryEntity
import com.example.expensetracker.domain.models.TransactionCategory
import kotlinx.coroutines.flow.Flow

interface TransactionCategoryRepository {

    suspend fun saveTransactionCategory(transactionCategory: TransactionCategory)

    fun getAllTransactionCategories(): Flow<List<TransactionCategoryEntity>>

    suspend fun getTransactionCategoryById(transactionId:String):TransactionCategoryEntity?

    suspend fun getTransactionCategoryByName(name:String): TransactionCategoryEntity?

    suspend fun updateTransactionCategory(transactionCategoryId:String,transactionCategoryName:String)

    suspend fun deleteTransactionCategory(transactionCategoryId:String)


}