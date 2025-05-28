package com.example.expensetracker.domain.use_case.income_useCase

import com.example.expensetracker.core.util.Resource
import com.example.expensetracker.core.util.Response
import com.example.expensetracker.domain.models.Income
import com.example.expensetracker.domain.repository.IncomeRepository
import com.example.expensetracker.domain.Mappers.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class CreateIncomeUseCase @Inject constructor(
    private val incomeRepository: IncomeRepository,
) {
    operator fun invoke(income: Income): Flow<Resource<Response>> = flow {
        try {
            emit(Resource.Loading())
            incomeRepository.insertIncome(incomeEntity = income.toEntity())
            emit(Resource.Success(data = Response(msg = "Income saved successfully")))
        } catch (e: IOException) {
            emit(Resource.Error(message = e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}