package com.example.expensetracker.ui.bottomsheets.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.core.util.Resource
import com.example.expensetracker.core.util.UiEvent
import com.example.expensetracker.domain.models.ExpenseCategory
import com.example.expensetracker.domain.use_case.expenseCategory_useCase.CreateExpenseCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject


data class AddExpenseCategoryUiState(
    val expenseCategoryName: String = "",
    val isLoading: Boolean = false
)

@HiltViewModel
class AddExpenseCategoryScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val createExpenseCategoryUseCase: CreateExpenseCategoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddExpenseCategoryUiState())
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onChangeExpenseName(text: String) {
        _uiState.value = _uiState.value.copy(expenseCategoryName = text)
    }

    fun addExpenseCategory() {
        viewModelScope.launch {
            if (_uiState.value.expenseCategoryName.isBlank()) {
                _eventFlow.emit(UiEvent.ShowSnackBar(uiText = "Expense Category Name cannot be black"))
            } else {
                val expenseCategory = ExpenseCategory(
                    expenseCategoryName = _uiState.value.expenseCategoryName,
                    expenseCategoryId = UUID.randomUUID().toString()
                )
                createExpenseCategoryUseCase(expenseCategory = expenseCategory).onEach { result ->
                    when (result) {
                        is Resource.Loading -> {
                            _uiState.value = _uiState.value.copy(isLoading = true)
                        }

                        is Resource.Success -> {
                            _eventFlow.emit(
                                UiEvent.ShowSnackBar(
                                    uiText = result.data?.msg
                                        ?: "Expense Category added successfully"
                                )
                            )
                            _uiState.value = _uiState.value.copy(
                                expenseCategoryName = "",
                                isLoading = false
                            )
                        }

                        is Resource.Error -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false
                            )
                            _eventFlow.emit(
                                UiEvent.ShowSnackBar(
                                    uiText = result.message ?: "An unexpected  error occurred"
                                )
                            )
                        }
                    }
                }.launchIn(this)
            }
        }
    }
}