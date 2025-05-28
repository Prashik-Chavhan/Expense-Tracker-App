package com.example.expensetracker.domain.use_case.expenseCategory_useCase

import com.example.expensetracker.core.util.Resource
import com.example.expensetracker.core.util.Response
import com.example.expensetracker.domain.models.ExpenseCategory
import com.example.expensetracker.domain.repository.ExpenseCategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import javax.inject.Inject

class CreateExpenseCategoryUseCase @Inject constructor(
    private val repository: ExpenseCategoryRepository
){

    suspend operator fun invoke(expenseCategory: ExpenseCategory): Flow<Resource<Response>> = flow {
        try {
            emit(value = Resource.Loading())
            val existingExpenseCategory =
                repository.getExpenseCategoryByName(name = expenseCategory.expenseCategoryName)
            if (existingExpenseCategory == null) {
                repository.saveExpenseCategory(expenseCategory = expenseCategory)
                emit(value = Resource.Success(Response(msg = "Expense Category Added")))
            } else {
                emit(value = Resource.Error(message = "An expense category with a similar name already exists"))
            }
        } catch (e: IOException) {
            emit(
                value = Resource.Error(
                    message = e.localizedMessage ?: "An unexpected error occurred"
                )
            )
        }
    }
}