package com.example.expensetracker.ui.screens.transactions

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.expensetracker.R
import com.example.expensetracker.core.util.FilterConstants
import com.example.expensetracker.ui.components.CustomTopAppBar
import com.example.expensetracker.ui.components.DashboardFinanceCard
import com.example.expensetracker.ui.components.EmptyComponent
import com.example.expensetracker.ui.components.ErrorComponent
import com.example.expensetracker.ui.components.FilterCard
import com.example.expensetracker.ui.components.LoadingComponent

@Composable
fun AllTransactionsScreen(
    modifier: Modifier = Modifier,
    viewModel: AllTransactionsScreenViewModel = hiltViewModel(),
    onTransactionCardClicked: (String) -> Unit,
    onNavBackClicked: () -> Unit
) {
    val activeTransactionFilter by viewModel.activeTransactionFilter.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AllTransactionsScreenContent(
        activeTransactionFilter = activeTransactionFilter,
        uiState = uiState,
        onChangeActiveTransactionFilter = { viewModel.onChangeActiveTransactionFilter(it) },
        onTransactionCardClicked = { transactionId -> onTransactionCardClicked(transactionId) },
        onNavBackClicked = { onNavBackClicked() },
        modifier = modifier
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllTransactionsScreenContent(
    activeTransactionFilter: String,
    uiState: AllTransactionsScreenUiState,
    onChangeActiveTransactionFilter: (String) -> Unit,
    onTransactionCardClicked: (String) -> Unit,
    onNavBackClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = stringResource(id = R.string.my_transactions),
                onNavBackClicked = { onNavBackClicked() }
            )
        }
    ) { paddingValues ->
        when (uiState) {
            is AllTransactionsScreenUiState.Loading -> {
                LoadingComponent()
            }

            is AllTransactionsScreenUiState.Error -> {
                ErrorComponent(message = uiState.message)
            }

            is AllTransactionsScreenUiState.Success -> {
                val transactionCategories = uiState.transactionCategories
                val transactions = uiState.transactions
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        item {
                            FilterCard(
                                filterName = FilterConstants.ALL,
                                isActive = activeTransactionFilter == FilterConstants.ALL,
                                onClick = {
                                    onChangeActiveTransactionFilter(FilterConstants.ALL)
                                }
                            )
                        }
                        items(items = transactionCategories) { category ->
                            FilterCard(
                                filterName = category.transactionCategoryName,
                                isActive = activeTransactionFilter == category.transactionCategoryId,
                                onClick = {
                                    onChangeActiveTransactionFilter(category.transactionCategoryId)
                                }
                            )
                        }
                    }
                    if (transactions.isEmpty()) {
                        val name = if (activeTransactionFilter == FilterConstants.ALL) "All" else transactionCategories.find { it.transactionCategoryId == activeTransactionFilter }?.transactionCategoryName
                        EmptyComponent(
                            message = "No transactions in $name",
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(items = transactions) { transaction ->
                                DashboardFinanceCard(
                                    id = transaction.transactionId,
                                    name = transaction.transactionName,
                                    amount = transaction.transactionAmount,
                                    createdAt = transaction.transactionCreatedOn,
                                    onItemClicked = { transactionId ->
                                        onTransactionCardClicked(
                                            transactionId
                                        )
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