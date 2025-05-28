package com.example.expensetracker.domain.use_case.transaction_useCase

import com.example.expensetracker.domain.repository.TransactionRepository
import javax.inject.Inject

class DeleteTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository
) {

    suspend operator fun invoke(transactionId:String){
        return repository.deleteTransactionById(transactionId = transactionId)
    }

}