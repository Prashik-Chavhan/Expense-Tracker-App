package com.example.expensetracker.core.util

import com.example.expensetracker.domain.models.ExpenseCategory
import com.example.expensetracker.domain.models.TransactionCategory
import java.util.UUID

data class ExampleTransactionCategory(
    val name: String,
)

data class ExampleExpenseCategory(
    val name: String
)

val sampleTransactionCategories = listOf<ExampleTransactionCategory>(
    ExampleTransactionCategory("Salary"),
    ExampleTransactionCategory("Freelance"),
    ExampleTransactionCategory("Business Income"),
    ExampleTransactionCategory("Investment Returns"),
    ExampleTransactionCategory("Gift Received"),
    ExampleTransactionCategory("Refund"),
    ExampleTransactionCategory("Loan Received"),
    ExampleTransactionCategory("Interest Income"),
    ExampleTransactionCategory("Rental Income"),
    ExampleTransactionCategory("Cashback/Rewards")
)

val sampleExpenseCategories = listOf<ExampleExpenseCategory>(
    ExampleExpenseCategory("Food & Dining"),
    ExampleExpenseCategory("Groceries"),
    ExampleExpenseCategory("Transportation"),
    ExampleExpenseCategory("Rent"),
    ExampleExpenseCategory("Utilities"),
    ExampleExpenseCategory("Internet & Mobile"),
    ExampleExpenseCategory("Entertainment"),
    ExampleExpenseCategory("Shopping"),
    ExampleExpenseCategory("Health & Fitness"),
    ExampleExpenseCategory("Education"),
    ExampleExpenseCategory("Travel"),
    ExampleExpenseCategory("Insurance"),
    ExampleExpenseCategory("Loan EMI"),
    ExampleExpenseCategory("Subscriptions"),
    ExampleExpenseCategory("Miscellaneous")
)

fun ExampleTransactionCategory.toTransactionCategory(): TransactionCategory {
    return TransactionCategory(
        transactionCategoryName = name,
        transactionCategoryId = UUID.randomUUID().toString(),
    )
}

fun ExampleExpenseCategory.toExpenseCategory(): ExpenseCategory {
    return ExpenseCategory(
        expenseCategoryName = name,
        expenseCategoryId = UUID.randomUUID().toString()
    )
}