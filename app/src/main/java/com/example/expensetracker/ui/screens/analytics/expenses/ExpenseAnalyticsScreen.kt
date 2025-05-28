package com.example.expensetracker.ui.screens.analytics.expenses

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.expensetracker.core.util.generateRandomColor
import com.example.expensetracker.domain.models.Expense
import com.example.expensetracker.domain.models.ExpenseCategory
import com.example.expensetracker.domain.Mappers.toExternalModel
import com.github.tehras.charts.piechart.PieChart
import com.github.tehras.charts.piechart.PieChartData
import com.github.tehras.charts.piechart.animation.simpleChartAnimation
import com.github.tehras.charts.piechart.renderer.SimpleSliceDrawer

data class PieDataInfo(
    val slice: PieChartData.Slice,
    val expenseCategory: ExpenseCategory,
)

@Composable
fun ExpensesAnalyticsScreen(
    modifier: Modifier = Modifier,
    viewModel: ExpenseAnalyticsScreenViewModel = hiltViewModel(),
) {
    val expensesCategoriesInfo = viewModel.expenseCategories.value
    val pieData = expensesCategoriesInfo.map { expenseCategoryInfoEntity ->
        val total = expenseCategoryInfoEntity.expenses
            .collectAsStateWithLifecycle(initialValue = emptyList())
            .value
            .map { it.toExternalModel() }
            .map { it.expenseAmount.toFloat() }
            .sum()
        PieDataInfo(
            slice = PieChartData.Slice(
                value = total,
                color = generateRandomColor()
            ),
            expenseCategory = expenseCategoryInfoEntity.expenseCategory
        )
    }

    val expenses = expensesCategoriesInfo.map { it.expenses }
        .map { it.collectAsStateWithLifecycle(initialValue = emptyList()) }
        .map { it.value }
        .flatten()
        .map { it.toExternalModel() }

    ExpensesAnalyticsScreenContent(
        pieData = pieData,
        expenseCategories = expensesCategoriesInfo.map { it.expenseCategory },
        expenses = expenses
    )
}

@Composable
fun ExpensesAnalyticsScreenContent(
    pieData: List<PieDataInfo>,
    expenseCategories: List<ExpenseCategory>,
    expenses: List<Expense>,
    modifier: Modifier = Modifier
) {
    Scaffold() { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(10.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                ) {
                    PieChart(
                        pieChartData = PieChartData(slices = pieData.map { pieDataInfo -> pieDataInfo.slice }),
                        modifier = Modifier
                            .size(210.dp)
                            .padding(10.dp),
                        animation = simpleChartAnimation(),
                        sliceDrawer = SimpleSliceDrawer(),
                    )
                }
            }
            items(items = pieData) {pieDataInfo ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(30.dp)
                            .height(20.dp)
                            .background(color = pieDataInfo.slice.color)
                            .padding(3.dp)
                    )
                    Text(
                        text = pieDataInfo.expenseCategory.expenseCategoryName,
                        modifier = Modifier.padding(3.dp),
                        fontSize = 17.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "INR " + pieDataInfo.slice.value.toString() + "/=",
                        modifier = Modifier.padding(3.dp),
                        fontSize = 17.sp,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
    }
}