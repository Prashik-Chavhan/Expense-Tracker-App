package com.example.expensetracker.ui.screens.transaction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.domain.models.Transaction
import com.example.expensetracker.domain.models.TransactionInfo
import com.example.expensetracker.domain.use_case.transaction_useCase.DeleteTransactionUseCase
import com.example.expensetracker.domain.use_case.transaction_useCase.GetSingleTransactionUseCase

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DeleteTransactionUiState(
    val isDeleteTransactionDialogVisible:Boolean = false,
    val deleteTransaction: Transaction? = null
)

sealed interface TransactionScreenUiState {
    object Loading:TransactionScreenUiState

    data class Success(val transactionInfo: TransactionInfo):TransactionScreenUiState

    data class Error(val message:String):TransactionScreenUiState
}

@HiltViewModel
class TransactionScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getSingleTransactionUseCase: GetSingleTransactionUseCase,
    private val deleteTransactionUseCase:DeleteTransactionUseCase,

) : ViewModel(){

    val transactionId = savedStateHandle.get<String>("id")

    private val _deleteTransactionUiState = MutableStateFlow(DeleteTransactionUiState())
    val deleteTransactionUiState = _deleteTransactionUiState.asStateFlow()

    private val _uiState = MutableStateFlow<TransactionScreenUiState>(TransactionScreenUiState.Loading)
    val uiState = _uiState.asStateFlow()


    init {

        viewModelScope.launch {
            if (transactionId == null){
                _uiState.value = TransactionScreenUiState.Error(message = "Transaction Not Found")
            }else{
                val info = getSingleTransactionUseCase(transactionId = transactionId)
                if (info == null){
                    _uiState.value = TransactionScreenUiState.Error(message = "Transaction Not Found")
                }else{
                    _uiState.value = TransactionScreenUiState.Success(transactionInfo = info)
                }
            }
        }
    }
    fun toggleDeleteDialogVisibility(){
        val initialState = _deleteTransactionUiState.value.isDeleteTransactionDialogVisible
        _deleteTransactionUiState.update {
            it.copy(isDeleteTransactionDialogVisible = !initialState)
        }
    }
    fun setDeleteTransaction(transaction: Transaction?){
        _deleteTransactionUiState.update {
            it.copy(deleteTransaction = transaction)
        }
    }

    fun deleteTransaction(onSuccess:() -> Unit){
        viewModelScope.launch {
            if (_uiState.value is TransactionScreenUiState.Success){
                if (transactionId != null) {
                    deleteTransactionUseCase(transactionId = transactionId)
                    toggleDeleteDialogVisibility()
                    setDeleteTransaction(null)
                    onSuccess()
                }
            }
        }
    }
}