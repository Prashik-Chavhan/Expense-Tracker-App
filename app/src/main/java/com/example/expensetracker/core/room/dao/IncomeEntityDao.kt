package com.example.expensetracker.core.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.expensetracker.core.room.entities.IncomeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IncomeEntityDao {

    @Query("SELECT * FROM Income")
    fun getAllIncome(): Flow<List<IncomeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncome(incomeEntity: IncomeEntity)

    @Query("DELETE FROM Income WHERE incomeId = :id")
    suspend fun deleteIncomeById(id: String)

    @Query("SELECT * FROM Income WHERE incomeId = :id")
    fun getIncomeById(id: String): Flow<IncomeEntity>

    @Query("DELETE FROM Income")
    suspend fun deleteAllIncome()
}