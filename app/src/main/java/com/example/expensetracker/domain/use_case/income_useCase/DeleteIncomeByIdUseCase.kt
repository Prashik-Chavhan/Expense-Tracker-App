package com.example.expensetracker.domain.use_case.income_useCase

import com.example.expensetracker.domain.repository.IncomeRepository
import javax.inject.Inject

class DeleteIncomeByIdUseCase @Inject constructor(
    private val repository: IncomeRepository
) {
    suspend operator fun invoke(incomeId:String){
        return repository.deleteIncomeById(id = incomeId)
    }
}