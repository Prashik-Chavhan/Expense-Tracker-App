package com.example.expensetracker.domain.use_case.searchTransaction_useCase

import com.example.expensetracker.core.room.entities.TransactionEntity
import com.example.expensetracker.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchTransactionsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(startDate: String, endDate: String,categoryId:String): Flow<List<TransactionEntity>> {
        return repository.searchTransactions(startDate = startDate, endDate = endDate,categoryId = categoryId)
    }
}