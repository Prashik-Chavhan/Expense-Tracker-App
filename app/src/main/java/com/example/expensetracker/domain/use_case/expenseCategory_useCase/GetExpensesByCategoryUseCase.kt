package com.example.expensetracker.domain.use_case.expenseCategory_useCase

import com.example.expensetracker.core.room.entities.ExpenseEntity
import com.example.expensetracker.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExpensesByCategoryUseCase @Inject constructor(
    private val repository: ExpenseRepository
) {

    operator fun invoke(expenseCategoryId:String): Flow<List<ExpenseEntity>> {
        return repository.getExpensesByCategory(categoryId = expenseCategoryId)
    }



}