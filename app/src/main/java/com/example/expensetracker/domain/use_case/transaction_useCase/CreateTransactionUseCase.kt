package com.example.expensetracker.domain.use_case.transaction_useCase

import com.example.expensetracker.core.util.Resource
import com.example.expensetracker.core.util.Response
import com.example.expensetracker.domain.models.Transaction
import com.example.expensetracker.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import javax.inject.Inject

class CreateTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction): Flow<Resource<Response>> = flow {
        try {
            emit(Resource.Loading())
            repository.createTransaction(transaction = transaction)
            emit(Resource.Success(data = Response(msg = "Transaction saved successfully")))
        } catch (e: IOException) {
            emit(Resource.Error(message = e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}