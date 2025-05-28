package com.example.expensetracker.core.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "TransactionCategory")
data class TransactionCategoryEntity(
    @PrimaryKey
    val transactionCategoryId:String,
    val transactionCategoryName:String,

)