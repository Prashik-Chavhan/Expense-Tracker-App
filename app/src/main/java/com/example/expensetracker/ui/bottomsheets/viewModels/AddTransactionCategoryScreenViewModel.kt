package com.example.expensetracker.ui.bottomsheets.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.core.util.Resource
import com.example.expensetracker.core.util.UiEvent
import com.example.expensetracker.domain.models.TransactionCategory
import com.example.expensetracker.domain.use_case.transactionCategory_useCase.CreateTransactionCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

data class AddTransactionCategoryFormState(
    val transactionCategoryName: String = "",
    val isLoading: Boolean = false
)


@HiltViewModel
class AddTransactionCategoryScreenViewModel @Inject constructor(
    private val createTransactionCategoryUseCase: CreateTransactionCategoryUseCase,


    ) : ViewModel() {

    private val _formState = MutableStateFlow(AddTransactionCategoryFormState())
    val formState = _formState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onChangeTransactionCategoryName(text: String) {
        _formState.value = _formState.value.copy(transactionCategoryName = text)

    }


    fun addTransactionCategory() {
        viewModelScope.launch {
            if (_formState.value.transactionCategoryName.isBlank()) {
                _eventFlow.emit(UiEvent.ShowSnackBar(uiText = "Transaction Category Name cannot be black"))
            } else {
                val transactionCategory = TransactionCategory(
                    transactionCategoryName = _formState.value.transactionCategoryName,
                    transactionCategoryId = UUID.randomUUID().toString(),

                )
                createTransactionCategoryUseCase(transactionCategory = transactionCategory).onEach { result ->
                    when (result) {
                        is Resource.Loading -> {
                            _formState.value = _formState.value.copy(isLoading = true)

                        }

                        is Resource.Success -> {
                            _eventFlow.emit(
                                UiEvent.ShowSnackBar(
                                    uiText = result.data?.msg
                                        ?: "Transaction Category added successfully"
                                )
                            )
                            _formState.value = _formState.value.copy(
                                isLoading = false,
                                transactionCategoryName = ""
                            )
                        }

                        is Resource.Error -> {

                            _eventFlow.emit(
                                UiEvent.ShowSnackBar(
                                    uiText = result.message ?: "An error occurred"
                                )
                            )
                            _formState.value = _formState.value.copy(
                                isLoading = false,
                            )
                        }
                    }
                }.launchIn(this)


            }
        }

    }


}