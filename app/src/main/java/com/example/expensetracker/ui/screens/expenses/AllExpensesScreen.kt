package com.example.expensetracker.ui.screens.expenses

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.expensetracker.core.util.FilterConstants
import com.example.expensetracker.ui.components.CustomTopAppBar
import com.example.expensetracker.ui.components.DashboardFinanceCard
import com.example.expensetracker.ui.components.EmptyComponent
import com.example.expensetracker.ui.components.ErrorComponent
import com.example.expensetracker.ui.components.FilterCard
import com.example.expensetracker.ui.components.LoadingComponent

@Composable
fun AllExpensesScreen(
    navigateToExpenseScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AllExpensesScreenViewModel = hiltViewModel(),
    onNavBackClicked: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val activeExpenseFilter by viewModel.activeExpenseFilter.collectAsStateWithLifecycle()

    AllExpensesScreenContent(
        activeExpenseFilter = activeExpenseFilter,
        uiState = uiState,
        onChangeActiveExpenseFilter = {
            viewModel.onChangeActiveExpenseFilter(it)
        },
        navigateToExpenseScreen = navigateToExpenseScreen,
        onNavBackClicked = { onNavBackClicked() },
        modifier = modifier
    )
}

@Composable
fun AllExpensesScreenContent(
    activeExpenseFilter: String,
    uiState: AllExpensesScreenUiState,
    onChangeActiveExpenseFilter: (String) -> Unit,
    navigateToExpenseScreen: (String) -> Unit,
    onNavBackClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CustomTopAppBar(
                title = "My Expenses",
                onNavBackClicked = { onNavBackClicked() }
            )
        },
    ) { paddingValues ->
        when (uiState) {
            is AllExpensesScreenUiState.Loading -> {
                LoadingComponent()
            }

            is AllExpensesScreenUiState.Error -> {
                ErrorComponent(message = uiState.message)
            }

            is AllExpensesScreenUiState.Success -> {
                val expenseCategories = uiState.expenseCategories
                val expenses = uiState.expenses
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        item {
                            FilterCard(
                                filterName = FilterConstants.ALL,
                                isActive = activeExpenseFilter == FilterConstants.ALL,
                                onClick = {
                                    onChangeActiveExpenseFilter(FilterConstants.ALL)
                                }
                            )
                        }
                        items(
                            items = expenseCategories,
                            key = { it.expenseCategoryId }) { category ->
                            FilterCard(
                                filterName = category.expenseCategoryName,
                                isActive = activeExpenseFilter == category.expenseCategoryId,
                                onClick = {
                                    onChangeActiveExpenseFilter(category.expenseCategoryId)

                                }
                            )
                        }
                    }
                    if (expenses.isEmpty()) {
                        val name = if (activeExpenseFilter == FilterConstants.ALL) "All" else expenseCategories.find { it.expenseCategoryId == activeExpenseFilter }?.expenseCategoryName
                        EmptyComponent(
                            message = "No expenses in $name",
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(items = expenses, key = { it.expenseId }) { expense ->
                                val isVisible =
                                    if (activeExpenseFilter == FilterConstants.ALL) true else (activeExpenseFilter == expense.expenseCategoryId)
                                AnimatedVisibility(
                                    visible = isVisible
                                ) {
                                    DashboardFinanceCard(
                                        id = expense.expenseId,
                                        name = expense.expenseName,
                                        amount = expense.expenseAmount,
                                        createdAt = expense.expenseCreatedOn,
                                        onItemClicked = { expenseId ->
                                            navigateToExpenseScreen(expenseId)
                                        },
                                        modifier = Modifier.padding(
                                            horizontal = 16.dp,
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
}