package com.example.expensetracker.core.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Income")
data class IncomeEntity(
    @PrimaryKey
    val incomeId: String,
    val incomeName: String,
    val incomeAmount: Int,
    val incomeCreatedAt: String
)