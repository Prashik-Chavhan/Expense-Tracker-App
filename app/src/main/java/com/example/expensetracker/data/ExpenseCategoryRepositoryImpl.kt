package com.example.expensetracker.data

import com.example.expensetracker.core.di.IoDispatcher
import com.example.expensetracker.core.room.database.ExpenseTrackerAppDatabase
import com.example.expensetracker.core.room.entities.ExpenseCategoryEntity
import com.example.expensetracker.domain.Mappers.toEntity
import com.example.expensetracker.domain.models.ExpenseCategory
import com.example.expensetracker.domain.repository.ExpenseCategoryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExpenseCategoryRepositoryImpl @Inject constructor(
    private val db:ExpenseTrackerAppDatabase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
): ExpenseCategoryRepository {
    override suspend fun saveExpenseCategory(expenseCategory: ExpenseCategory) {
        return withContext(ioDispatcher){
            db.expenseCategoryEntityDao.insertExpenseCategory(
                expenseCategoryEntity = expenseCategory.toEntity())
        }
    }

    override suspend fun getExpenseCategoryById(expenseCategoryId: String): ExpenseCategoryEntity? {
        return withContext(ioDispatcher){
            db.expenseCategoryEntityDao.getExpenseCategoryById(expenseCategoryId = expenseCategoryId)
        }
    }

    override fun getAllExpenseCategories(): Flow<List<ExpenseCategoryEntity>> {
        return db.expenseCategoryEntityDao.getExpenseCategories().flowOn(ioDispatcher)
    }

    override suspend fun getExpenseCategoryByName(name: String): ExpenseCategoryEntity? {
        return withContext(ioDispatcher){
            db.expenseCategoryEntityDao.getExpenseCategoryByName(name = name)
        }

    }

    override suspend fun updateExpenseCategory(expenseCategoryId: String, expenseCategoryName: String) {
        withContext(ioDispatcher){
            db.expenseCategoryEntityDao.updateExpenseCategoryName(
                id = expenseCategoryId, name = expenseCategoryName)
        }
    }

    override suspend fun deleteExpenseCategory(expenseCategoryId: String) {
        withContext(ioDispatcher){
            db.expenseCategoryEntityDao.deleteExpenseCategoryById(id = expenseCategoryId)
        }
    }

}