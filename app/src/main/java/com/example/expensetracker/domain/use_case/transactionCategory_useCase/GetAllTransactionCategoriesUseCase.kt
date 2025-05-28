package com.example.expensetracker.domain.use_case.transactionCategory_useCase

import com.example.expensetracker.core.room.entities.TransactionCategoryEntity
import com.example.expensetracker.domain.repository.TransactionCategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllTransactionCategoriesUseCase @Inject constructor(
    private val repository: TransactionCategoryRepository
){

    operator fun invoke(): Flow<List<TransactionCategoryEntity>> {
        return repository.getAllTransactionCategories()
    }
}