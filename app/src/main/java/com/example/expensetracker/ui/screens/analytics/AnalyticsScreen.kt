package com.example.expensetracker.ui.screens.analytics

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensetracker.R
import com.example.expensetracker.ui.components.CustomTopAppBar
import com.example.expensetracker.ui.screens.analytics.expenses.ExpensesAnalyticsScreen
import com.example.expensetracker.ui.screens.analytics.transactions.TransactionsAnalyticsScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AnalyticsScreen(
    onTransactionCardClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = stringResource(id = R.string.analytics),
                canNavigateBack = false,
                onNavBackClicked = { }
            )
        }
    ) { paddingValues ->
        val pagerState = rememberPagerState(pageCount = { 2 })
        Column(
            modifier = modifier
                .padding(paddingValues)
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            Tabs(pagerState = pagerState)
            TabsContent(
                pagerState = pagerState,
                onTransactionCardClicked = { transactionId -> onTransactionCardClicked(transactionId) }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Tabs(pagerState: PagerState) {
    val list = listOf(
        stringResource(id = R.string.transactions),
        stringResource(id = R.string.expenses),
    )
    val scope = rememberCoroutineScope()

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        divider = { },
        indicator = { tabPositions ->
            SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(currentTabPosition = tabPositions[pagerState.currentPage]),
                height = 2.dp,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    ) {
        list.forEachIndexed { index, label ->
            Tab(
                text = {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabsContent(
    pagerState: PagerState,
    onTransactionCardClicked: (String) -> Unit
) {
    HorizontalPager(state = pagerState) { page ->
        when (page) {
            0 -> TransactionsAnalyticsScreen(
                onTransactionCardClicked = { transactionId -> onTransactionCardClicked(transactionId) }
            )
            1 -> ExpensesAnalyticsScreen()
        }
    }
}
