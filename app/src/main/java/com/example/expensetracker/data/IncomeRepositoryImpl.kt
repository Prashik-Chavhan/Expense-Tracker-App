package com.example.expensetracker.data

import com.example.expensetracker.core.di.IoDispatcher
import com.example.expensetracker.core.room.database.ExpenseTrackerAppDatabase
import com.example.expensetracker.core.room.entities.IncomeEntity
import com.example.expensetracker.domain.repository.IncomeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class IncomeRepositoryImpl @Inject constructor(
    private val db: ExpenseTrackerAppDatabase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
): IncomeRepository {

    override suspend fun insertIncome(incomeEntity: IncomeEntity) {
        withContext(ioDispatcher){
            db.incomeEntityDao.insertIncome(incomeEntity = incomeEntity)
        }
    }

    override  fun getAllIncome(): Flow<List<IncomeEntity>> {
        return db.incomeEntityDao.getAllIncome().flowOn(ioDispatcher)
    }

    override fun getIncomeById(id:String): Flow<IncomeEntity?> {
        return db.incomeEntityDao.getIncomeById(id = id).flowOn(ioDispatcher)
    }

    override suspend fun deleteIncomeById(id: String) {
        withContext(ioDispatcher){
            db.incomeEntityDao.deleteIncomeById(id = id)
        }
    }

    override suspend fun deleteAllIncome() {
        withContext(ioDispatcher){
            db.incomeEntityDao.deleteAllIncome()
        }
    }
}