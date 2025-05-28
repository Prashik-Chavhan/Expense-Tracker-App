package com.example.expensetracker.ui.screens.income

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.domain.models.Income
import com.example.expensetracker.domain.Mappers.toExternalModel
import com.example.expensetracker.domain.use_case.income_useCase.DeleteIncomeByIdUseCase
import com.example.expensetracker.domain.use_case.income_useCase.GetIncomeByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface IncomeScreenUiState {
    object Loading : IncomeScreenUiState

    data class Success(val income: Income) : IncomeScreenUiState

    data class Error(val message: String) : IncomeScreenUiState
}

data class DeleteIncomeUiState(
    val isDeleteIncomeDialogVisible: Boolean = false,
    val deleteIncome: Income? = null
)

@HiltViewModel
class IncomeScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getIncomeByIdUseCase: GetIncomeByIdUseCase,
    private val deleteIncomeByIdUseCase: DeleteIncomeByIdUseCase,
) : ViewModel() {

    val incomeId = savedStateHandle.get<String>("id")

    private val _deleteIncomeUiState = MutableStateFlow(DeleteIncomeUiState())
    val deleteIncomeUiState = _deleteIncomeUiState.asStateFlow()

    private val _uiState = MutableStateFlow<IncomeScreenUiState>(IncomeScreenUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getIncomeById()
    }

    fun getIncomeById() {
        viewModelScope.launch {
            if (incomeId == null) {
                _uiState.value = IncomeScreenUiState.Error("Income not found")
            } else {
                val income = getIncomeByIdUseCase(incomeId = incomeId)

                if (false) {
                    _uiState.value = IncomeScreenUiState.Error("Income not found")
                } else {
                    income.collectLatest {
                        it?.let {
                            _uiState.value = IncomeScreenUiState.Success(it.toExternalModel())
                        }
                    }
                }
            }
        }
    }

    fun toggleDeleteDialogVisibility() {
        val initialState = _deleteIncomeUiState.value.isDeleteIncomeDialogVisible

        _deleteIncomeUiState.update {
            it.copy(isDeleteIncomeDialogVisible = !initialState)
        }
    }

    fun setDeleteIncome(income: Income?) {
        _deleteIncomeUiState.update {
            it.copy(deleteIncome = income)
        }
    }

    fun deleteIncome(onSuccess: () -> Unit) {
        viewModelScope.launch {
            if (_uiState.value is IncomeScreenUiState.Success && incomeId != null) {
                deleteIncomeByIdUseCase(incomeId = incomeId)
                toggleDeleteDialogVisibility()
                setDeleteIncome(null)
                onSuccess()
            }
        }
    }
}