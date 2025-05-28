package com.example.expensetracker.ui.screens.transaction

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.expensetracker.R
import com.example.expensetracker.domain.models.Transaction
import com.example.expensetracker.ui.alertDialogs.ConfirmDeleteDialog
import com.example.expensetracker.ui.components.CustomTopAppBar
import com.example.expensetracker.ui.components.ErrorComponent
import com.example.expensetracker.ui.components.LoadingComponent
import com.example.expensetracker.ui.screens.expense.Detail_Item_Card

@Composable
fun TransactionScreen(
    modifier: Modifier = Modifier,
    viewModel: TransactionScreenViewModel = hiltViewModel(),
    onNavBackClicked: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val deleteTransactionUiState by viewModel.deleteTransactionUiState.collectAsStateWithLifecycle()

    TransactionScreenContent(
        onNavBackClicked = { onNavBackClicked() },
        deleteTransactionUiState = deleteTransactionUiState,
        uiState = uiState,
        deleteTransaction = {
            viewModel.deleteTransaction(
                onSuccess = { onNavBackClicked() }
            )
        },
        setDeleteTransaction = viewModel::setDeleteTransaction,
        toggleDeleteTransactionVisibility = viewModel::toggleDeleteDialogVisibility
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreenContent(
    onNavBackClicked: () -> Unit,
    deleteTransactionUiState: DeleteTransactionUiState,
    uiState: TransactionScreenUiState,
    deleteTransaction: () -> Unit,
    setDeleteTransaction: (Transaction?) -> Unit,
    toggleDeleteTransactionVisibility: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = stringResource(id = R.string.transactions),
                onNavBackClicked = { onNavBackClicked() }
            )
        }
    ) { paddingValues ->
        when (uiState) {
            is TransactionScreenUiState.Loading -> {
                LoadingComponent()
            }

            is TransactionScreenUiState.Error -> {
                ErrorComponent(message = uiState.message)
            }

            is TransactionScreenUiState.Success -> {
                val transactionInfo = uiState.transactionInfo
                if (deleteTransactionUiState.isDeleteTransactionDialogVisible &&
                    deleteTransactionUiState.deleteTransaction != null
                ) {
                    ConfirmDeleteDialog(
                        closeDeleteDialog = { toggleDeleteTransactionVisibility() },
                        type = "transaction",
                        title = deleteTransactionUiState.deleteTransaction.transactionName,
                        onDeleteClicked = { deleteTransaction() }
                    )
                }
                transactionInfo.let { transactionInfo ->
                    val transaction = transactionInfo.transaction
                    val transactionCategory = transactionInfo.category

                    Detail_Item_Card(
                        id = transaction?.transactionId,
                        name = transaction?.transactionName,
                        amount = transaction?.transactionAmount,
                        createdOn = transaction?.transactionCreatedOn,
                        createdAt = transaction?.transactionCreatedAt,
                        categoryName = transactionCategory?.transactionCategoryName,
                        onDeleteClicked = {
                            setDeleteTransaction(transactionInfo.transaction)
                            toggleDeleteTransactionVisibility()
                        },
                        modifier = modifier.padding(paddingValues),
                    )
                }
            }
        }
    }
}