package com.example.expensetracker.ui.screens.expense

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.expensetracker.R
import com.example.expensetracker.domain.models.Expense
import com.example.expensetracker.ui.alertDialogs.ConfirmDeleteDialog
import com.example.expensetracker.ui.components.CustomTopAppBar
import com.example.expensetracker.ui.components.ErrorComponent
import com.example.expensetracker.ui.components.LoadingComponent

@Composable
fun ExpenseScreen(
    modifier: Modifier = Modifier,
    viewModel: ExpenseScreenViewModel = hiltViewModel(),
    onNavBackClicked: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val deleteExpenseUiState by viewModel.deleteExpenseUiState.collectAsStateWithLifecycle()
    ExpenseScreenContent(
        uiState = uiState,
        deleteExpenseUiState = deleteExpenseUiState,
        setDeleteExpense = viewModel::setDeleteExpense,
        toggleDeleteExpenseDialog = viewModel::toggleDeleteExpenseDialogVisibility,
        deleteExpense = { viewModel.deleteExpense(onNavBackClicked) },
        onNavBackClicked = { onNavBackClicked() }
    )
}

@Composable
fun ExpenseScreenContent(
    uiState: ExpenseScreenUiState,
    deleteExpenseUiState: DeleteExpenseUiState,
    setDeleteExpense: (Expense?) -> Unit,
    toggleDeleteExpenseDialog: () -> Unit,
    deleteExpense: () -> Unit,
    onNavBackClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CustomTopAppBar(
                title = stringResource(id = R.string.expense),
                onNavBackClicked = { onNavBackClicked() }
            )
        }
    ) { paddingValues ->
        when (uiState) {
            is ExpenseScreenUiState.Loading -> {
                LoadingComponent()
            }

            is ExpenseScreenUiState.Error -> {
                ErrorComponent(message = uiState.message)
            }

            is ExpenseScreenUiState.Success -> {
                val expenseInfo = uiState.expenseInfo
                if (deleteExpenseUiState.isDeleteExpenseDialogVisible &&
                    deleteExpenseUiState.deleteExpense != null
                ) {
                    ConfirmDeleteDialog(
                        closeDeleteDialog = { toggleDeleteExpenseDialog() },
                        type = "expense",
                        title = deleteExpenseUiState.deleteExpense.expenseName,
                        onDeleteClicked = { deleteExpense() }
                    )
                }
                expenseInfo.let { expenseInfo ->
                    val expense = expenseInfo.expense
                    val expenseCategory = expenseInfo.expenseCategory

                    Detail_Item_Card(
                        id = expense?.expenseId,
                        name = expense?.expenseName,
                        amount = expense?.expenseAmount,
                        createdOn = expense?.expenseCreatedOn,
                        createdAt = expense?.expenseCreatedAt,
                        categoryName = expenseCategory.expenseCategoryName,
                        onDeleteClicked = {
                            setDeleteExpense(expenseInfo.expense)
                            toggleDeleteExpenseDialog()
                        },
                        modifier = Modifier.padding(paddingValues),
                    )
                }
            }
        }
    }
}

@Composable
fun Detail_Item_Card(
    id: String?,
    name: String?,
    amount: Int?,
    createdOn: String?,
    createdAt: String?,
    categoryName: String?,
    onDeleteClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(10.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Transaction ID: $id",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Name: $name",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Normal
                    )
                    Text(
                        text = "Amount: ₹$amount",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Normal
                    )
                    if (createdOn?.isNotEmpty() == true) {
                        Text(
                            text = "Created On: $createdOn",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Normal
                        )
                    }
                    Text(
                        text = "Created At: $createdAt",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Normal
                    )
                    if (categoryName?.isNotEmpty() == true) {
                        HorizontalDivider(
                            modifier = Modifier.padding(top = 8.dp),
                            thickness = 2.dp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "Category: $categoryName",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                contentColor = Color.White
            ),
            onClick = { onDeleteClicked() }
        ) {
            Text(
                text = stringResource(id = R.string.delete_transaction),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}