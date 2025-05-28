package com.example.expensetracker.ui.screens.income

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.expensetracker.R
import com.example.expensetracker.domain.models.Income
import com.example.expensetracker.ui.alertDialogs.ConfirmDeleteDialog
import com.example.expensetracker.ui.components.CustomTopAppBar
import com.example.expensetracker.ui.components.ErrorComponent
import com.example.expensetracker.ui.components.LoadingComponent
import com.example.expensetracker.ui.screens.expense.Detail_Item_Card

@Composable
fun IncomeScreen(
    modifier: Modifier = Modifier,
    viewModel: IncomeScreenViewModel = hiltViewModel(),
    onNavBackClicked: () -> Unit,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val deleteIncomeUiState = viewModel.deleteIncomeUiState.collectAsStateWithLifecycle()

    IncomeScreenContent(
        onNavBackClicked = { onNavBackClicked() },
        uiState = uiState.value,
        deleteIncomeUiState = deleteIncomeUiState.value,
        setDeleteIncome = viewModel::setDeleteIncome,
        toggleDeleteIncomeDialog = viewModel::toggleDeleteDialogVisibility,
        deleteIncome = {
            viewModel.deleteIncome(
                onSuccess = { onNavBackClicked() }
            )
        }
    )
}

@Composable
fun IncomeScreenContent(
    onNavBackClicked: () -> Unit,
    uiState: IncomeScreenUiState,
    deleteIncomeUiState: DeleteIncomeUiState,
    setDeleteIncome: (Income?) -> Unit,
    toggleDeleteIncomeDialog: () -> Unit,
    deleteIncome: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = stringResource(id = R.string.income),
                onNavBackClicked = { onNavBackClicked() }
            )
        }
    ) { paddingValues ->
        when (uiState) {
            is IncomeScreenUiState.Loading -> {
                LoadingComponent()
            }

            is IncomeScreenUiState.Error -> {
                ErrorComponent(message = uiState.message)
            }

            is IncomeScreenUiState.Success -> {
                val income = uiState.income
                if (deleteIncomeUiState.isDeleteIncomeDialogVisible &&
                    deleteIncomeUiState.deleteIncome != null
                ) {
                    ConfirmDeleteDialog(
                        closeDeleteDialog = { toggleDeleteIncomeDialog() },
                        type = "income",
                        title = income.incomeName,
                        onDeleteClicked = { deleteIncome() }
                    )
                }
                Detail_Item_Card(
                    id = income.incomeId,
                    name = income.incomeName,
                    amount = income.incomeAmount,
                    createdOn = "",
                    createdAt = income.incomeCreatedAt,
                    categoryName = "",
                    onDeleteClicked = {
                        setDeleteIncome(income)
                        toggleDeleteIncomeDialog()
                    },
                    modifier = modifier.padding(paddingValues),
                )
            }
        }
    }
}