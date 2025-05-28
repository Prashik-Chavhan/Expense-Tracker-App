package com.example.expensetracker.domain.use_case.expenseCategory_useCase

import com.example.expensetracker.core.room.entities.ExpenseCategoryEntity
import com.example.expensetracker.domain.repository.ExpenseCategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllExpenseCategoriesUseCase @Inject constructor(
    private val repository: ExpenseCategoryRepository
) {
    operator fun invoke(): Flow<List<ExpenseCategoryEntity>> {
        return repository.getAllExpenseCategories()
    }
}