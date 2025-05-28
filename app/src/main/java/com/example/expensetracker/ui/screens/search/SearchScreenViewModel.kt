package com.example.expensetracker.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.core.util.DateUtil.datesBetween
import com.example.expensetracker.core.util.DateUtil.generateFormatDate
import com.example.expensetracker.core.util.UiEvent
import com.example.expensetracker.domain.models.Transaction
import com.example.expensetracker.domain.models.TransactionCategory
import com.example.expensetracker.domain.Mappers.toExternalModel
import com.example.expensetracker.domain.use_case.transactionCategory_useCase.GetAllTransactionCategoriesUseCase
import com.example.expensetracker.domain.use_case.searchTransaction_useCase.SearchTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject
import android.util.Log

data class SearchScreenState(
    val startDate: LocalDate = LocalDate.now(),
    val endDate: LocalDate = LocalDate.now(),
    val transactionCategory: TransactionCategory? = null,
    val transactions: List<Transaction> = emptyList()
)

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val getAllTransactionCategoriesUseCase: GetAllTransactionCategoriesUseCase,
    private val searchTransactionsUseCase: SearchTransactionsUseCase
) : ViewModel() {
    val transactionCategories = getAllTransactionCategoriesUseCase()

    private val _uiState = MutableStateFlow(SearchScreenState())
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onChangeTransactionStartDate(date: LocalDate) {
        _uiState.update { it.copy(startDate = date) }
    }

    fun onChangeTransactionEndDate(date: LocalDate) {
        _uiState.update { it.copy(endDate = date) }
    }

    fun onChangeTransactionCategory(category: TransactionCategory) {
        _uiState.update { it.copy(transactionCategory = category) }
    }

    fun searchTransactions() {
        viewModelScope.launch {
            if (_uiState.value.transactionCategory == null) {
                _eventFlow.emit(UiEvent.ShowSnackBar(uiText = "Please select a transaction category"))
                return@launch
            }
            searchTransactionsUseCase(
                startDate = generateFormatDate(_uiState.value.startDate),
                endDate = generateFormatDate(_uiState.value.endDate),
                categoryId = _uiState.value.transactionCategory!!.transactionCategoryId
            ).collect { transactions ->
                _uiState.update { initialState ->
                    val transactionMap = transactions.map { it.toExternalModel() }
                    initialState.copy(
                        transactions = transactionMap
                    )
                }
            }
        }
    }
}