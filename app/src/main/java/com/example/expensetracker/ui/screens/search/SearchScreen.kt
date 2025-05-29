package com.example.expensetracker.ui.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.expensetracker.R
import com.example.expensetracker.core.util.DateUtil.generateFormatDate
import com.example.expensetracker.core.util.UiEvent
import com.example.expensetracker.core.util.convertTimeMillisToLocalDate
import com.example.expensetracker.domain.Mappers.toExternalModel
import com.example.expensetracker.domain.models.TransactionCategory
import com.example.expensetracker.ui.components.CustomTopAppBar
import com.example.expensetracker.ui.components.DashboardFinanceCard
import com.example.expensetracker.ui.components.MenuSample
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate

@Composable
fun SearchScreen(
    onTransactionCardClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchScreenViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    val transactionCategories = viewModel.transactionCategories
        .collectAsStateWithLifecycle(initialValue = emptyList())
        .value
        .map { it.toExternalModel() }

    SearchScreenContent(
        uiState = uiState.value,
        transactionCategories = transactionCategories,
        searchTransactions = { viewModel.searchTransactions() },
        onTransactionCategoryChange = viewModel::onChangeTransactionCategory,
        onChangeTransactionStartDate = viewModel::onChangeTransactionStartDate,
        onChangeTransactionEndDate = viewModel::onChangeTransactionEndDate,
        onTransactionCardClicked = { transactionId -> onTransactionCardClicked(transactionId) },
        eventFlow = viewModel.eventFlow,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreenContent(
    onTransactionCardClicked: (String) -> Unit,
    uiState: SearchScreenState,
    transactionCategories: List<TransactionCategory>,
    searchTransactions: () -> Unit,
    onTransactionCategoryChange: (TransactionCategory) -> Unit,
    onChangeTransactionStartDate: (LocalDate) -> Unit,
    onChangeTransactionEndDate: (LocalDate) -> Unit,
    eventFlow: SharedFlow<UiEvent>,
    modifier: Modifier = Modifier
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val startDatePickerState = rememberDatePickerState()
    val endDatePickerState = rememberDatePickerState()
    var message by remember { mutableStateOf("") }

    val showStartPicker = remember { mutableStateOf(false) }
    val selectedStartDate = remember { mutableStateOf<Long?>(null) }
    val showEndPicker = remember { mutableStateOf(false) }
    val selectedEndDate = remember { mutableStateOf<Long?>(null) }

    if (showStartPicker.value) {
        DatePickerDialog(
            onDismissRequest = { showStartPicker.value = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedStartDate.value = startDatePickerState.selectedDateMillis
                        showStartPicker.value = false
                    }
                ) {
                    Text(
                        text = "Confirm"
                    )
                }
            },
            content = {
                DatePicker(
                    state = startDatePickerState
                )
            }
        )
    }

    if (showEndPicker.value) {
        DatePickerDialog(
            onDismissRequest = { showEndPicker.value = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedEndDate.value = endDatePickerState.selectedDateMillis
                        showEndPicker.value = false
                    }
                ) {
                    Text("Confirm")
                }
            }
        ) {
            DatePicker(
                state = endDatePickerState
            )
        }
    }

    LaunchedEffect(
        key1 = startDatePickerState.selectedDateMillis,
        key2 = startDatePickerState.selectedDateMillis
    ) {
        startDatePickerState.selectedDateMillis?.let {
            onChangeTransactionStartDate(convertTimeMillisToLocalDate(it))
        }
        endDatePickerState.selectedDateMillis?.let {
            onChangeTransactionEndDate(convertTimeMillisToLocalDate(it))
        }
    }

    LaunchedEffect(key1 = true) {
        eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(
                        message = event.uiText
                    )
                    message = event.uiText
                }

                is UiEvent.Navigate -> {

                }

                else -> {}
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        topBar = {
            CustomTopAppBar(
                title = stringResource(id = R.string.search),
                canNavigateBack = false,
                onNavBackClicked = {}
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            item {
                val startLocalDate =
                    selectedStartDate.value?.let { convertTimeMillisToLocalDate(it) }
                val selectedStartDate = startLocalDate?.let { generateFormatDate(it) }
                val endLocalDate = selectedEndDate.value?.let { convertTimeMillisToLocalDate(it) }
                val selectedEndDate = endLocalDate?.let { generateFormatDate(it) }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(
                        16.dp,
                        alignment = Alignment.CenterHorizontally
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Date_Card(
                            label = "Start date: ",
                            date = selectedStartDate ?: generateFormatDate(uiState.startDate),
                            onIconClicked = { showStartPicker.value = true }
                        )
                        Date_Card(
                            label = "End date: ",
                            date = selectedEndDate ?: generateFormatDate(uiState.endDate),
                            onIconClicked = { showEndPicker.value = true }
                        )
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val remWidth = LocalWindowInfo.current.containerSize.width
                        val selectedIndex = uiState.transactionCategory?.let { cat ->
                            transactionCategories.indexOfFirst { it.transactionCategoryId == cat.transactionCategoryId }
                        } ?: 0

                        MenuSample(
                            label = "Category:",
                            menuWidth = remWidth,
                            selectedIndex = selectedIndex,
                            menuItems = transactionCategories.map { it.transactionCategoryName },
                            onSelectedIndexChanged = { idx ->
                                onTransactionCategoryChange(transactionCategories[idx])
                            }
                        )
                        Button(
                            onClick = { searchTransactions() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Icon(Icons.Default.Search, contentDescription = null)
                            Spacer(Modifier.width(4.dp))
                            Text("Search")
                        }
                    }

                }
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (uiState.transactions.isEmpty()) {
                        Text(
                            text = "No transactions found ",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Text(
                            text = "A total of ${uiState.transactions.size} transactions have been made " +
                                    "between ${uiState.startDate}" +
                                    " and ${uiState.endDate}",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
            items(items = uiState.transactions) { transaction ->
                DashboardFinanceCard(
                    id = transaction.transactionId,
                    name = transaction.transactionName,
                    amount = transaction.transactionAmount,
                    createdAt = transaction.transactionCreatedOn,
                    onItemClicked = { transactionId ->
                        onTransactionCardClicked(transactionId)
                    }
                )
            }
        }
    }
}

@Composable
private fun Date_Card(
    label: String,
    date: String,
    onIconClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onBackground
        )
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(start = 8.dp)
        )
        Row(
            modifier = Modifier.padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = date,
                style = MaterialTheme.typography.bodyLarge
            )
            IconButton(
                onClick = { onIconClicked() }
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Select date"
                )
            }
        }
    }
}