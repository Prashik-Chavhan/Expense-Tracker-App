package com.example.expensetracker.ui.bottomsheets.viewModels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.core.util.DateUtil.generateFormatDate
import com.example.expensetracker.core.util.Resource
import com.example.expensetracker.core.util.UiEvent
import com.example.expensetracker.core.util.isNumeric
import com.example.expensetracker.domain.models.Expense
import com.example.expensetracker.domain.models.ExpenseCategory
import com.example.expensetracker.domain.use_case.expense_useCase.CreateExpenseUseCase
import com.example.expensetracker.domain.use_case.expenseCategory_useCase.GetAllExpenseCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.UUID
import javax.inject.Inject

data class AddExpenseFormState(
    val isLoading: Boolean = false,
    val expenseName: String = "",
    val expenseAmount: Int = 0,
    val selectedExpenseCategory: ExpenseCategory? = null
)

@HiltViewModel
class AddExpenseScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getAllExpenseCategoriesUseCase: GetAllExpenseCategoriesUseCase,
    private val createExpenseUseCase: CreateExpenseUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(AddExpenseFormState())
    val uiState = _uiState.asStateFlow()

    val expenseCategories = getAllExpenseCategoriesUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onChangeExpenseName(text: String) {
        _uiState.value = _uiState.value.copy(expenseName = text)
    }

    fun onChangeExpenseAmount(text: String) {
        if (text.isBlank()) {
            _uiState.value = _uiState.value.copy(expenseAmount = 0)

            return
        }
        if (isNumeric(text)) {
            _uiState.value = _uiState.value.copy(expenseAmount = text.toInt())


        } else {

            _uiState.value = _uiState.value.copy(expenseAmount = 0)
        }
    }

    fun onChangeSelectedExpenseCategory(category: ExpenseCategory) {
        _uiState.value = _uiState.value.copy(selectedExpenseCategory = category)
    }

    fun addExpense() {
        viewModelScope.launch {
            if (_uiState.value.expenseAmount == 0 || _uiState.value.expenseName == "") {
                _eventFlow.emit(
                    UiEvent.ShowSnackBar(
                        uiText = "Please enter a valid expense"
                    )
                )
                return@launch
            }
            if (_uiState.value.selectedExpenseCategory == null) {
                _eventFlow.emit(
                    UiEvent.ShowSnackBar(
                        uiText = "Please select an expense category"
                    )
                )
                return@launch
            }
            val newExpense = Expense(
                expenseId = UUID.randomUUID().toString(),
                expenseName = _uiState.value.expenseName,
                expenseAmount = _uiState.value.expenseAmount,
                expenseCategoryId = _uiState.value.selectedExpenseCategory!!.expenseCategoryId,
                expenseCreatedAt = SimpleDateFormat("hh:mm:ss").format(Date()),
                expenseUpdatedAt = SimpleDateFormat("hh:mm:ss").format(Date()),
                expenseCreatedOn = generateFormatDate(date = LocalDate.now()),
                expenseUpdatedOn = generateFormatDate(date = LocalDate.now())
            )
            createExpenseUseCase(expense = newExpense).onEach { result ->
                when (result) {
                    is Resource.Loading -> {

                        _uiState.value = _uiState.value.copy(isLoading = true)

                    }

                    is Resource.Success -> {
                        _eventFlow.emit(
                            UiEvent.ShowSnackBar(
                                uiText = result.data?.msg ?: "Expense  added successfully"
                            )
                        )
                        _uiState.value = _uiState.value.copy(
                            expenseName = "",
                            expenseAmount = 0,
                            selectedExpenseCategory = null,
                            isLoading = false,
                        )
                    }

                    is Resource.Error -> {
                        _uiState.value = _uiState.value.copy(isLoading = false)
                        _eventFlow.emit(
                            UiEvent.ShowSnackBar(
                                uiText = result.message ?: "An error occurred"
                            )
                        )

                    }
                }
            }.launchIn(this)
        }
    }
}