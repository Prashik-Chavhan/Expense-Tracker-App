package com.example.expensetracker.domain.use_case.searchTransaction_useCase

import com.example.expensetracker.core.room.entities.TransactionEntity
import com.example.expensetracker.core.util.DateUtil.datesBetween
import com.example.expensetracker.core.util.DateUtil.generate7daysPriorDate
import com.example.expensetracker.core.util.DateUtil.generateFormatDate
import com.example.expensetracker.core.util.DateUtil.getMonthDates
import com.example.expensetracker.core.util.DateUtil.getWeekDates
import com.example.expensetracker.core.util.DateUtil.previousDay
import com.example.expensetracker.core.util.FilterConstants
import com.example.expensetracker.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import javax.inject.Inject

class GetFilteredTransactionsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    val todayDate = generateFormatDate(date = LocalDate.now())
    val yesterdayDate = previousDay(date = todayDate)
    val weekDates = getWeekDates(dateString = todayDate)
    val monthDates = getMonthDates(dateString = todayDate)
    val oneWeekEarlierDate = generate7daysPriorDate(date = todayDate)
    val daysInThe7daysPrior = datesBetween(startDate = oneWeekEarlierDate, endDate = todayDate)

    operator fun invoke(filter: String): Flow<List<TransactionEntity>> {
        when (filter) {
            FilterConstants.TODAY -> {
                return repository.getTransactionsForACertainDay(date = todayDate)
            }
            FilterConstants.YESTERDAY -> {
                return repository.getTransactionsForACertainDay(date = yesterdayDate)
            }
            FilterConstants.THIS_WEEK -> {
                return repository.getTransactionsBetweenTwoDates(dates = weekDates)
            }
            FilterConstants.LAST_7_DAYS -> {
                return repository.getTransactionsBetweenTwoDates(dates = daysInThe7daysPrior)
            }
            FilterConstants.THIS_MONTH -> {
                return repository.getTransactionsBetweenTwoDates(dates = monthDates)
            }
            FilterConstants.ALL -> {
                return repository.getAllTransactions()
            }
            else ->  {
                return flow { emptyList<TransactionEntity>() }
            }
        }

    }
}