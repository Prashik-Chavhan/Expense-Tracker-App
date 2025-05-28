package com.example.expensetracker.ui.screens.incomes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.domain.models.Income
import com.example.expensetracker.domain.use_case.income_useCase.GetAllIncomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed interface AllIncomeScreenUiState {
    object Loading:AllIncomeScreenUiState

    data class Success(val incomes:List<Income>):AllIncomeScreenUiState

    data class Error(val message:String):AllIncomeScreenUiState
}


@HiltViewModel
class AllIncomeScreenViewModel @Inject constructor(
    private val getAllIncomeUseCase: GetAllIncomeUseCase,
): ViewModel() {

    val uiState = getAllIncomeUseCase()
        .map {
            AllIncomeScreenUiState.Success(it)
        }
        .onStart { AllIncomeScreenUiState.Loading }
        .catch { AllIncomeScreenUiState.Error(message = "Failed to your data") }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = AllIncomeScreenUiState.Loading
        )
}