package com.example.expensetracker.domain.use_case.income_useCase

import com.example.expensetracker.core.room.entities.IncomeEntity
import com.example.expensetracker.domain.repository.IncomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetIncomeByIdUseCase @Inject constructor(
    private val repository: IncomeRepository
) {

    operator fun invoke(incomeId:String): Flow<IncomeEntity?> {
        return repository.getIncomeById(id = incomeId)
    }
}