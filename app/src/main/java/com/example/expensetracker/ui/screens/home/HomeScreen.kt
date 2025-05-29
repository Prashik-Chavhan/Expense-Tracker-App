package com.example.expensetracker.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.expensetracker.R
import com.example.expensetracker.core.util.UiEvent
import com.example.expensetracker.ui.bottomsheets.view.AddExpenseBottomSheet
import com.example.expensetracker.ui.bottomsheets.view.AddExpenseCategoryBottomSheet
import com.example.expensetracker.ui.bottomsheets.view.AddIncomeBottomSheet
import com.example.expensetracker.ui.bottomsheets.view.AddTransactionBottomSheet
import com.example.expensetracker.ui.bottomsheets.view.AddTransactionCategoryBottomSheet
import com.example.expensetracker.ui.components.CustomTopAppBar
import com.example.expensetracker.ui.components.DashboardFinanceCard
import com.example.expensetracker.ui.components.EmptyComponent
import com.example.expensetracker.ui.components.ErrorComponent
import com.example.expensetracker.ui.components.HomeScreenActionsGrid
import com.example.expensetracker.ui.components.HomeSubHeader
import com.example.expensetracker.ui.components.LoadingComponent
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAllIncomeClicked: () -> Unit,
    onIncomeCardClicked: (String) -> Unit,
    onAllTransactionsClicked: () -> Unit,
    onTransactionCardClicked: (String) -> Unit,
    onAllExpensesClicked: () -> Unit,
    onExpenseCardClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: HomeScreenViewModel = hiltViewModel()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    HomeScreenContent(
        uiState = uiState,
        eventFlow = viewModel.eventFlow,
        sheetState = sheetState,
        activeBottomSheet = viewModel.activeBottomSheet.value,
        onBottomSheetChange = { bottomSheet ->
            viewModel.onChangeActiveBottomSheet(bottomSheet)
        },
        onAllIncomeClicked = { onAllIncomeClicked() },
        onIncomeCardClicked = { incomeId -> onIncomeCardClicked(incomeId) },
        onAllTransactionsClicked = { onAllTransactionsClicked() },
        onTransactionCardClicked = { transactionId -> onTransactionCardClicked(transactionId) },
        onAllExpensesClicked = { onAllExpensesClicked() },
        onExpenseCardClicked = { expenseId -> onExpenseCardClicked(expenseId) },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    uiState: HomeScreenUiState,
    eventFlow: SharedFlow<UiEvent>,
    sheetState: SheetState,
    activeBottomSheet: BottomSheets?,
    onBottomSheetChange: (BottomSheets) -> Unit,
    onAllIncomeClicked: () -> Unit,
    onIncomeCardClicked: (String) -> Unit,
    onAllTransactionsClicked: () -> Unit,
    onTransactionCardClicked: (String) -> Unit,
    onAllExpensesClicked: () -> Unit,
    onExpenseCardClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showBottomSheet by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackBar -> {

                }

                is UiEvent.Navigate -> {

                }

                is UiEvent.OpenBottomSheet -> {
                    showBottomSheet = true
                }
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            onDismissRequest = { showBottomSheet = false },
            sheetMaxWidth = BottomSheetDefaults.SheetMaxWidth
        ) {
            when (activeBottomSheet) {
                BottomSheets.ADD_EXPENSE -> {
                    AddExpenseBottomSheet(
                        onClicked = { showBottomSheet = false }
                    )
                }

                BottomSheets.ADD_EXPENSE_CATEGORY -> {
                    AddExpenseCategoryBottomSheet(
                        onClicked = { showBottomSheet = false }
                    )
                }

                BottomSheets.ADD_TRANSACTION -> {
                    AddTransactionBottomSheet(
                        onClicked = { showBottomSheet = false }
                    )
                }

                BottomSheets.ADD_TRANSACTION_CATEGORY -> {
                    AddTransactionCategoryBottomSheet(
                        onClicked = { showBottomSheet = false }
                    )
                }

                BottomSheets.ADD_INCOME -> {
                    AddIncomeBottomSheet(
                        onAddIncome = { showBottomSheet = false }
                    )
                }

                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = stringResource(id = R.string.app_name),
                canNavigateBack = false,
                onNavBackClicked = {}
            )
        }
    ) { paddingValues ->
        when (uiState) {
            is HomeScreenUiState.Loading -> {
                LoadingComponent()
            }

            is HomeScreenUiState.Error -> {
                ErrorComponent(message = uiState.message)
            }

            is HomeScreenUiState.Success -> {
                val totalIncome = uiState.income.sumOf { it.incomeAmount }
                val totalExpense = uiState.expenses.sumOf { it.expenseAmount }
                val totalTransaction = uiState.transactions.sumOf { it.transactionAmount }

                val remainingIncome = totalIncome - (totalExpense + totalTransaction)
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp)
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .height(100.dp),
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Your Income ",
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "INR $remainingIncome /=",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 30.sp,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                        item {
                            HomeScreenActionsGrid { index ->
                                when (index) {
                                    0 -> {
                                        onBottomSheetChange(
                                            BottomSheets.ADD_INCOME
                                        )
                                    }

                                    1 -> {
                                        onBottomSheetChange(
                                            BottomSheets.ADD_TRANSACTION
                                        )
                                    }

                                    2 -> {
                                        onBottomSheetChange(
                                            BottomSheets.ADD_EXPENSE
                                        )
                                    }

                                    3 -> {
                                        onBottomSheetChange(
                                            BottomSheets.ADD_TRANSACTION_CATEGORY
                                        )
                                    }

                                    4 -> {
                                        onBottomSheetChange(
                                            BottomSheets.ADD_EXPENSE_CATEGORY
                                        )
                                    }
                                }
                            }
                        }
                        item {
                            HomeSubHeader(
                                name = "Income",
                                onClick = {
                                    onAllIncomeClicked()
                                }
                            )
                        }
                        if (uiState.income.isEmpty()) {
                            item {
                                EmptyComponent(message = "No income saved")
                            }
                        } else {
                            items(
                                items = uiState.income.take(n = 2),
                                key = { it.incomeId }) { income ->
                                DashboardFinanceCard(
                                    id = income.incomeId,
                                    name = income.incomeName,
                                    amount = income.incomeAmount,
                                    createdAt = income.incomeCreatedAt,
                                    onItemClicked = { incomeId ->
                                        onIncomeCardClicked(incomeId)
                                    },
                                    modifier = Modifier.padding(
                                        vertical = 8.dp
                                    )
                                )
                            }
                        }

                        item {
                            HomeSubHeader(
                                name = "Transactions",
                                onClick = {
                                    onAllTransactionsClicked()
                                }
                            )
                        }
                        if (uiState.transactions.isEmpty()) {
                            item {
                                EmptyComponent(message = "No transactions saved")
                            }
                        } else {
                            items(
                                items = uiState.transactions.take(n = 2),
                                key = { it.transactionId }) { transaction ->
                                DashboardFinanceCard(
                                    id = transaction.transactionId,
                                    name = transaction.transactionName,
                                    amount = transaction.transactionAmount,
                                    createdAt = transaction.transactionCreatedOn,
                                    onItemClicked = { transactionId ->
                                        onTransactionCardClicked(transactionId)
                                    },
                                    modifier = Modifier.padding(
                                        vertical = 8.dp
                                    )
                                )
                            }
                        }
                        item {
                            HomeSubHeader(
                                name = "Expenses",
                                onClick = {
                                    onAllExpensesClicked()
                                }
                            )
                        }
                        if (uiState.expenses.isEmpty()) {
                            item {
                                EmptyComponent(message = "No expenses saved")
                            }
                        } else {
                            items(
                                items = uiState.expenses.take(n = 2),
                                key = { it.expenseId }
                            ) { expense ->
                                DashboardFinanceCard(
                                    id = expense.expenseId,
                                    name = expense.expenseName,
                                    amount = expense.expenseAmount,
                                    createdAt = expense.expenseUpdatedOn,
                                    onItemClicked = { expenseId ->
                                        onExpenseCardClicked(expenseId)
                                    },
                                    modifier = Modifier.padding(
                                        vertical = 8.dp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}