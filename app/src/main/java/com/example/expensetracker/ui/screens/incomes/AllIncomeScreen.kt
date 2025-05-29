package com.example.expensetracker.ui.screens.incomes

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.expensetracker.ui.components.CustomTopAppBar
import com.example.expensetracker.ui.components.DashboardFinanceCard
import com.example.expensetracker.ui.components.EmptyComponent
import com.example.expensetracker.ui.components.ErrorComponent
import com.example.expensetracker.ui.components.LoadingComponent

@Composable
fun AllIncomeScreen(
    modifier: Modifier = Modifier,
    viewModel: AllIncomeScreenViewModel = hiltViewModel(),
    onNavBackClicked: () -> Unit,
    onIncomeClicked: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AllIncomeScreenContent(
        uiState = uiState,
        onNavBackClicked = { onNavBackClicked() },
        onIncomeClicked = { incomeId -> onIncomeClicked(incomeId) },
        modifier = modifier
    )
}

@Composable
fun AllIncomeScreenContent(
    uiState: AllIncomeScreenUiState,
    onIncomeClicked: (String) -> Unit,
    onNavBackClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "My Income",
                onNavBackClicked = { onNavBackClicked() }
            )
        }
    ) { paddingValues ->
        when (uiState) {
            is AllIncomeScreenUiState.Loading -> {
                LoadingComponent()
            }

            is AllIncomeScreenUiState.Error -> {
                ErrorComponent(message = uiState.message)
            }

            is AllIncomeScreenUiState.Success -> {
                val allIncome = uiState.incomes

                if (allIncome.isEmpty()) {
                    EmptyComponent(
                        modifier = Modifier.fillMaxSize(),
                        message = "No Incomes"
                    )
                } else {
                    LazyColumn(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        items(items = uiState.incomes) { income ->
                            DashboardFinanceCard(
                                id = income.incomeId,
                                name = income.incomeName,
                                amount = income.incomeAmount,
                                createdAt = income.incomeCreatedAt,
                                onItemClicked = { incomeId ->
                                    onIncomeClicked(incomeId)
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