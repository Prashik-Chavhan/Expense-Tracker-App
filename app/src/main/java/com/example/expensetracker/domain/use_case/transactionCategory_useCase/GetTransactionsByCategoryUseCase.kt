package com.example.expensetracker.domain.use_case.transactionCategory_useCase

import com.example.expensetracker.core.room.entities.TransactionEntity
import com.example.expensetracker.core.util.FilterConstants
import com.example.expensetracker.domain.repository.TransactionRepository
import com.example.expensetracker.domain.use_case.searchTransaction_useCase.GetFilteredTransactionsUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTransactionsByCategoryUseCase @Inject constructor(
    private val getFilteredTransactionsUseCase: GetFilteredTransactionsUseCase,
    private val repository: TransactionRepository
) {
    operator fun invoke(categoryId: String): Flow<List<TransactionEntity>> {
        if (categoryId == FilterConstants.ALL){
            return getFilteredTransactionsUseCase(filter = FilterConstants.ALL)
        }else{
            return repository.getTransactionByCategory(categoryId = categoryId)
        }
    }
}