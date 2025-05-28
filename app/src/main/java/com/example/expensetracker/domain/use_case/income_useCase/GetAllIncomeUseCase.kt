package com.example.expensetracker.domain.use_case.income_useCase

import com.example.expensetracker.domain.models.Income
import com.example.expensetracker.domain.repository.IncomeRepository
import com.example.expensetracker.domain.Mappers.toExternalModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllIncomeUseCase @Inject constructor(
    private val repository: IncomeRepository
) {

    operator fun invoke(): Flow<List<Income>> {
        return repository.getAllIncome().map { it.map { it.toExternalModel() } }
    }
}