package com.example.expensetracker.core.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

import com.example.expensetracker.core.room.dao.ExpenseCategoryEntityDao
import com.example.expensetracker.core.room.dao.ExpenseEntityDao
import com.example.expensetracker.core.room.dao.IncomeEntityDao
import com.example.expensetracker.core.room.dao.TransactionCategoryEntityDao
import com.example.expensetracker.core.room.dao.TransactionEntityDao
import com.example.expensetracker.core.room.entities.ExpenseCategoryEntity
import com.example.expensetracker.core.room.entities.ExpenseEntity
import com.example.expensetracker.core.room.entities.IncomeEntity
import com.example.expensetracker.core.room.entities.TransactionCategoryEntity
import com.example.expensetracker.core.room.entities.TransactionEntity
import com.example.expensetracker.core.room.converters.DateConverter

@TypeConverters(DateConverter::class)
@Database(
    entities = [
        ExpenseCategoryEntity::class,
        TransactionCategoryEntity::class,
        ExpenseEntity::class,
        TransactionEntity::class,
        IncomeEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ExpenseTrackerAppDatabase : RoomDatabase() {

    abstract val expenseCategoryEntityDao: ExpenseCategoryEntityDao

    abstract val transactionCategoryEntityDao: TransactionCategoryEntityDao

    abstract val expenseEntityDao:ExpenseEntityDao

    abstract val transactionEntityDao:TransactionEntityDao

    abstract val incomeEntityDao:IncomeEntityDao

}