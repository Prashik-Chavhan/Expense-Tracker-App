package com.example.expensetracker.domain.use_case.transaction_useCase

import com.example.expensetracker.domain.models.TransactionInfo
import com.example.expensetracker.domain.repository.TransactionCategoryRepository
import com.example.expensetracker.domain.repository.TransactionRepository
import com.example.expensetracker.domain.Mappers.toExternalModel
import javax.inject.Inject

class GetSingleTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val transactionCategoryRepository: TransactionCategoryRepository
){

    suspend operator fun invoke(transactionId:String): TransactionInfo?{
        val transaction = transactionRepository.getTransactionById(transactionId = transactionId)
        val transactionCategory = transaction?.let {
            transactionCategoryRepository.getTransactionCategoryById(
                transactionId = it.transactionCategoryId)
        }
        if (transactionCategory != null) {
            return TransactionInfo(
                transaction = transaction.toExternalModel(),
                category = transactionCategory.toExternalModel(),
            )
        }else{
            return null
        }

    }
}