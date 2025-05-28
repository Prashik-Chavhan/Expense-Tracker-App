package com.example.expensetracker.ui.screens.transactions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.core.util.FilterConstants
import com.example.expensetracker.domain.models.Transaction
import com.example.expensetracker.domain.models.TransactionCategory
import com.example.expensetracker.domain.Mappers.toExternalModel
import com.example.expensetracker.domain.use_case.transactionCategory_useCase.GetAllTransactionCategoriesUseCase
import com.example.expensetracker.domain.use_case.transactionCategory_useCase.GetTransactionsByCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed interface AllTransactionsScreenUiState {
    object Loading:AllTransactionsScreenUiState

    data class Success(
        val transactions:List<Transaction>,
        val transactionCategories:List<TransactionCategory>,
    ):AllTransactionsScreenUiState

    data class Error(val message:String):AllTransactionsScreenUiState
}

@HiltViewModel
class AllTransactionsScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    getTransactionsByCategoryUseCase: GetTransactionsByCategoryUseCase,
    getAllTransactionCategoriesUseCase: GetAllTransactionCategoriesUseCase,
):ViewModel() {

    val activeTransactionFilter = savedStateHandle.getStateFlow(key = "filter", initialValue = FilterConstants.ALL)

    val uiState = combine(
        activeTransactionFilter,
        getAllTransactionCategoriesUseCase(),
        getTransactionsByCategoryUseCase(categoryId = activeTransactionFilter.value),
    ){ transactionFilter, transactionsCategoryEntities,transactionEntities ->

        val transactionCategories = transactionsCategoryEntities.map { it.toExternalModel() }
        val transactions = transactionEntities.map { it.toExternalModel() }
            .filter {
                if (activeTransactionFilter.value != FilterConstants.ALL){
                    it.transactionCategoryId == activeTransactionFilter.value
                }else{
                    true
                }
            }
        AllTransactionsScreenUiState.Success(
            transactions = transactions,
            transactionCategories = transactionCategories
        )
    }.onStart {
        AllTransactionsScreenUiState.Loading
    }.catch { AllTransactionsScreenUiState.Error(message = "Failed to fetch categories") }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = AllTransactionsScreenUiState.Loading
        )

    fun onChangeActiveTransactionFilter(filter:String){
        savedStateHandle["filter"] = filter

    }
}