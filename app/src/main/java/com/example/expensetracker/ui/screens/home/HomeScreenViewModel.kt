package com.example.expensetracker.ui.screens.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.core.util.FilterConstants
import com.example.expensetracker.core.util.UiEvent
import com.example.expensetracker.domain.models.Expense
import com.example.expensetracker.domain.models.Income
import com.example.expensetracker.domain.models.Transaction
import com.example.expensetracker.domain.Mappers.toExternalModel
import com.example.expensetracker.domain.use_case.expense_useCase.GetAllExpensesUseCase
import com.example.expensetracker.domain.use_case.income_useCase.GetAllIncomeUseCase
import com.example.expensetracker.domain.use_case.searchTransaction_useCase.GetFilteredTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface HomeScreenUiState {
    object Loading:HomeScreenUiState

    data class Success(
        val income:List<Income>,
        val expenses:List<Expense>,
        val transactions:List<Transaction>,
    ):HomeScreenUiState

    data class Error(val message:String):HomeScreenUiState
}


enum class BottomSheets  {
    ADD_TRANSACTION,
    ADD_TRANSACTION_CATEGORY,
    ADD_EXPENSE,
    ADD_EXPENSE_CATEGORY,
    ADD_INCOME
}

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getFilteredTransactionsUseCase: GetFilteredTransactionsUseCase,
    private val getAllIncomeUseCase: GetAllIncomeUseCase,
    private val getAllExpensesUseCase: GetAllExpensesUseCase,

) : ViewModel() {
    val uiState = combine(
        getAllIncomeUseCase(),
        getAllExpensesUseCase(),
        getFilteredTransactionsUseCase(FilterConstants.ALL)
    ) { incomes, expenseEntities ,transactionEntities ->
        val transactions = transactionEntities.map { it.toExternalModel() }
        val expenses = expenseEntities.map { it.toExternalModel() }
        HomeScreenUiState.Success(expenses = expenses, income = incomes, transactions = transactions)
    }.onStart { HomeScreenUiState.Loading }
        .catch { HomeScreenUiState.Error(message = "Failed to load your data") }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = HomeScreenUiState.Loading
        )

    private val _activeBottomSheet = mutableStateOf<BottomSheets?>(null)
    val activeBottomSheet: State<BottomSheets?> = _activeBottomSheet

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    fun onChangeActiveBottomSheet(bottomSheet: BottomSheets){
        _activeBottomSheet.value = bottomSheet
        viewModelScope.launch {
            _eventFlow.emit(UiEvent.OpenBottomSheet(bottomSheet = bottomSheet))
        }
    }
}