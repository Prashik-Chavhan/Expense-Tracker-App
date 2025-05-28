package com.example.expensetracker.domain.use_case.expense_useCase

import com.example.expensetracker.core.util.Resource
import com.example.expensetracker.core.util.Response
import com.example.expensetracker.domain.models.Expense
import com.example.expensetracker.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import javax.inject.Inject

class CreateExpenseUseCase @Inject constructor(
  private val repository: ExpenseRepository
) {
    suspend operator fun invoke(expense: Expense): Flow<Resource<Response>> = flow {
        try {
            emit(Resource.Loading())
            repository.createExpense(expense = expense)
            emit(Resource.Success(data = Response(msg = "Expense saved successfully")))
        } catch (e: IOException) {
            emit(Resource.Error(message = e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}