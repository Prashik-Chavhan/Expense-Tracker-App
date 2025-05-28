package com.example.expensetracker.data

import com.example.expensetracker.core.di.IoDispatcher
import com.example.expensetracker.core.room.database.ExpenseTrackerAppDatabase
import com.example.expensetracker.core.room.entities.ExpenseEntity
import com.example.expensetracker.domain.models.Expense
import com.example.expensetracker.domain.repository.ExpenseRepository
import com.example.expensetracker.domain.Mappers.toEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExpenseRepositoryImpl @Inject constructor(
    private val db:ExpenseTrackerAppDatabase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
): ExpenseRepository {
    override suspend fun createExpense(expense: Expense) {
        withContext(ioDispatcher){
            db.expenseEntityDao.insertExpense(expenseEntity = expense.toEntity())
        }
    }

    override suspend fun getExpenseById(expenseId: String): ExpenseEntity? {
        return withContext(ioDispatcher){
            db.expenseEntityDao.getExpenseById(expenseId)
        }
    }

    override fun getAllExpenses(): Flow<List<ExpenseEntity>> {
        return db.expenseEntityDao.getAllExpenses().flowOn(ioDispatcher)
    }

    override suspend fun deleteExpenseById(expenseId: String) {
        withContext(ioDispatcher){
            db.expenseEntityDao.deleteExpenseById(id = expenseId)
        }
    }
    override fun getExpensesByCategory(categoryId: String): Flow<List<ExpenseEntity>> {
        return db.expenseEntityDao.getExpensesByCategory(id = categoryId).flowOn(ioDispatcher)
    }

    override suspend fun updateExpense(
        expenseName: String,
        expenseAmount: Int,
        expenseUpdatedAt: String,
        expenseUpdatedOn: String
    ) {
        TODO("Not yet implemented")
    }


}