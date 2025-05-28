package com.example.expensetracker.ui.screens.expenses

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.core.util.FilterConstants
import com.example.expensetracker.domain.models.Expense
import com.example.expensetracker.domain.models.ExpenseCategory
import com.example.expensetracker.domain.Mappers.toExternalModel
import com.example.expensetracker.domain.use_case.expenseCategory_useCase.GetAllExpenseCategoriesUseCase
import com.example.expensetracker.domain.use_case.expense_useCase.GetAllExpensesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed interface AllExpensesScreenUiState {
    object Loading : AllExpensesScreenUiState

    data class Success(
        val expenses: List<Expense>,
        val expenseCategories: List<ExpenseCategory>,
    ) : AllExpensesScreenUiState

    data class Error(val message: String) : AllExpensesScreenUiState
}

@HiltViewModel
class AllExpensesScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getAllExpenseCategoriesUseCase: GetAllExpenseCategoriesUseCase,
    private val getAllExpensesUseCase: GetAllExpensesUseCase,
) : ViewModel() {

    val activeExpenseFilter = savedStateHandle.getStateFlow<String>(
        key = "filter", initialValue = FilterConstants.ALL
    )

    val uiState = combine(
        getAllExpenseCategoriesUseCase(),
        getAllExpensesUseCase()
    ) { expenseCategoryEntities, expenseEntities ->
        val expenseCategories = expenseCategoryEntities.map { it.toExternalModel() }
        val expenses = expenseEntities.map { it.toExternalModel() }
        AllExpensesScreenUiState.Success(
            expenseCategories = expenseCategories,
            expenses = expenses,
        )
    }.onStart {
        AllExpensesScreenUiState.Loading
    }.catch { AllExpensesScreenUiState.Error(message = "Failed to fetch categories") }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = AllExpensesScreenUiState.Loading
        )

    fun onChangeActiveExpenseFilter(filter: String) {
        savedStateHandle["filter"] = filter
    }

}