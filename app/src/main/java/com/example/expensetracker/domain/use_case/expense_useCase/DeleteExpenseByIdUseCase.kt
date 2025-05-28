package com.example.expensetracker.domain.use_case.expense_useCase

import com.example.expensetracker.domain.repository.ExpenseRepository
import javax.inject.Inject

class DeleteExpenseByIdUseCase @Inject constructor(
    private val expenseRepository: ExpenseRepository,
){
    suspend operator fun invoke(expenseId:String){
        expenseRepository.deleteExpenseById(expenseId)
    }
}