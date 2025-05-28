package com.example.expensetracker.domain.repository

import com.example.expensetracker.core.room.entities.IncomeEntity
import kotlinx.coroutines.flow.Flow

interface IncomeRepository {

    suspend fun insertIncome(incomeEntity: IncomeEntity)

    fun getAllIncome(): Flow<List<IncomeEntity>>

    fun getIncomeById(id:String): Flow<IncomeEntity?>

    suspend fun deleteIncomeById(id:String)

    suspend fun deleteAllIncome()
}