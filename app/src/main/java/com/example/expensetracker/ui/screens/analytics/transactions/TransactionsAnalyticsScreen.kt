package com.example.expensetracker.ui.screens.analytics.transactions

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.expensetracker.core.util.DateUtil.getActualDayOfWeek
import com.example.expensetracker.core.util.FilterConstants
import com.example.expensetracker.domain.Mappers.toExternalModel
import com.example.expensetracker.domain.models.Transaction
import com.example.expensetracker.ui.components.DashboardFinanceCard
import com.example.expensetracker.ui.components.GraphFilterCard
import com.github.tehras.charts.bar.BarChart
import com.github.tehras.charts.bar.BarChartData
import com.github.tehras.charts.bar.renderer.bar.SimpleBarDrawer
import com.github.tehras.charts.bar.renderer.label.SimpleValueDrawer
import com.github.tehras.charts.bar.renderer.xaxis.SimpleXAxisDrawer
import com.github.tehras.charts.bar.renderer.yaxis.SimpleYAxisDrawer
import com.github.tehras.charts.piechart.animation.simpleChartAnimation

@Composable
fun TransactionsAnalyticsScreen(
    onTransactionCardClicked: (String) -> Unit,
) {
    val viewModel: AnalyticsScreenViewModel = hiltViewModel()

    val transactionsState = viewModel.graphData.value
        .map { data -> data.collectAsStateWithLifecycle(initialValue = emptyList()) }
        .map { state -> state.value.map { it.toExternalModel() } }

    val transactions = transactionsState.map { a ->
        val total = a.map { it.transactionAmount.toFloat() }.sum()
        if (a.isNotEmpty()) {
            BarChartData.Bar(
                value = total,
                label = if (transactionsState.size == 7) {
                    getActualDayOfWeek(a[0].transactionCreatedOn).substring(0, 3)
                } else {
                    (transactionsState.indexOf(a) + 1).toString()
                },
                color = MaterialTheme.colorScheme.primary
            )
        } else {
            BarChartData.Bar(
                value = total,
                label = "",
                color = MaterialTheme.colorScheme.primary
            )
        }

    }

    LaunchedEffect(key1 = transactions) {
        viewModel.onChangeBarDataList(data = transactions)
    }

    TransactionsAnalyticsScreenContent(
        transactionsState = transactionsState.flatten(),
        transactions = transactionsState.flatten(),
        activeFilterConstant = viewModel.activeFilterConstant.value,
        bars = transactions,
        onTransactionCardClicked = { transactionId -> onTransactionCardClicked(transactionId) },
        onChangeActiveFilterConstant = { viewModel.onChangeActiveFilterConstant(it) }
    )
}

@Composable
fun TransactionsAnalyticsScreenContent(
    transactionsState: List<Transaction>,
    transactions: List<Transaction>,
    activeFilterConstant: String,
    bars: List<BarChartData.Bar>,
    onChangeActiveFilterConstant: (String) -> Unit,
    onTransactionCardClicked: (String) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "INR ${transactions.sumOf { transaction -> transaction.transactionAmount }} /=",
                        fontWeight = FontWeight.Bold,
                        fontSize = 21.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    when (activeFilterConstant) {
                        FilterConstants.THIS_WEEK -> {
                            Text(
                                text = "Total spent this week",
                                style = TextStyle(color = Color.Gray),
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp
                            )
                        }

                        FilterConstants.LAST_7_DAYS -> {
                            Text(
                                text = "Total spent in the last 7 days",
                                style = TextStyle(color = Color.Gray),
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp
                            )
                        }

                        FilterConstants.THIS_MONTH -> {
                            Text(
                                text = "Total spent this month",
                                style = TextStyle(color = Color.Gray),
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp
                            )
                        }

                        FilterConstants.ALL -> {
                            Text(
                                text = "Total spending",
                                style = TextStyle(color = Color.Gray),
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
            item {
                AnimatedVisibility(
                    visible = activeFilterConstant != FilterConstants.ALL,
                    enter = slideInHorizontally() + fadeIn()
                ) {
                    BarChart(
                        barChartData = BarChartData(bars = bars),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .padding(10.dp),
                        animation = simpleChartAnimation(),
                        barDrawer = SimpleBarDrawer(),
                        xAxisDrawer = SimpleXAxisDrawer(
                            axisLineThickness = 1.dp,
                            axisLineColor = MaterialTheme.colorScheme.onBackground
                        ),
                        yAxisDrawer = SimpleYAxisDrawer(
                            axisLineThickness = 1.dp,
                            axisLineColor = MaterialTheme.colorScheme.onBackground,
                            labelTextColor = MaterialTheme.colorScheme.onBackground
                        ),
                        labelDrawer = SimpleValueDrawer(
                            drawLocation = SimpleValueDrawer.DrawLocation.XAxis,
                            labelTextColor = MaterialTheme.colorScheme.onBackground
                        )
                    )
                }
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    GraphFilterCard(
                        filterName = FilterConstants.THIS_WEEK,
                        isActive = activeFilterConstant == FilterConstants.THIS_WEEK,
                        onClick = {
                            onChangeActiveFilterConstant(it)
                        }
                    )
                    GraphFilterCard(
                        filterName = FilterConstants.LAST_7_DAYS,
                        isActive = activeFilterConstant == FilterConstants.LAST_7_DAYS,
                        onClick = {
                            onChangeActiveFilterConstant(it)
                        }
                    )
                    GraphFilterCard(
                        filterName = FilterConstants.THIS_MONTH,
                        isActive = activeFilterConstant == FilterConstants.THIS_MONTH,
                        onClick = {
                            onChangeActiveFilterConstant(it)
                        }
                    )
                    GraphFilterCard(
                        filterName = FilterConstants.ALL,
                        isActive = activeFilterConstant == FilterConstants.ALL,
                        onClick = {
                            onChangeActiveFilterConstant(it)
                        }
                    )
                }
            }

            items(items = transactionsState.reversed()) { transaction ->
                DashboardFinanceCard(
                    id = transaction.transactionId,
                    name = transaction.transactionName,
                    amount = transaction.transactionAmount,
                    createdAt = transaction.transactionCreatedOn,
                    onItemClicked = { transactionId ->
                        onTransactionCardClicked(transactionId)
                    },
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

