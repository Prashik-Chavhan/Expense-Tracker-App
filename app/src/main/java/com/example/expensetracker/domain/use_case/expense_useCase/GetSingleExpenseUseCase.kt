package com.example.expensetracker.domain.use_case.expense_useCase

import com.example.expensetracker.domain.models.Expense
import com.example.expensetracker.domain.models.ExpenseCategory
import com.example.expensetracker.domain.repository.ExpenseCategoryRepository
import com.example.expensetracker.domain.repository.ExpenseRepository
import com.example.expensetracker.domain.Mappers.toExternalModel
import javax.inject.Inject


data class ExpenseInfo(
    val expense: Expense?,
    val expenseCategory: ExpenseCategory,
)

class GetSingleExpenseUseCase @Inject constructor(
    private val expenseRepository: ExpenseRepository,
    private val  expenseCategoryRepository: ExpenseCategoryRepository,
) {


    suspend operator fun invoke(expenseId:String): ExpenseInfo?{
        val expense = expenseRepository.getExpenseById(expenseId = expenseId)
        val expenseCategory = expense?.let {
            expenseCategoryRepository.getExpenseCategoryById(
                expenseCategoryId = it.expenseCategoryId
            )
        }
        if (expenseCategory != null) {
            return ExpenseInfo(
                expense = expense.toExternalModel(),
                expenseCategory = expenseCategory.toExternalModel(),
            )
        }else{
            return null
        }

    }
}