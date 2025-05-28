package com.example.expensetracker.domain.models

data class TransactionInfo(
    val transaction: Transaction?,
    val category: TransactionCategory?,
)