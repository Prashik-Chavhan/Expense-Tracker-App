package com.example.expensetracker.ui.screens.onboarding.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.expensetracker.core.util.sampleTransactionCategories
import com.example.expensetracker.ui.components.ExampleTransactionCategoryCard
import com.example.expensetracker.ui.screens.onboarding.OnboardingScreenViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SelectTransactionCategoryScreen(
    modifier: Modifier = Modifier,
    viewModel: OnboardingScreenViewModel
) {
    val selectedTransactionCategories by viewModel.selectedTransactionCategories.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(it)
                .padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "Create Transaction categories you'd like to record in the app",
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                lineHeight = 40.sp,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Start,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "You can create custom categories that were not captured here",
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(modifier = Modifier.height(10.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
            ) {
                sampleTransactionCategories.forEach { category ->
                    ExampleTransactionCategoryCard(
                        category = category,
                        isSelected = selectedTransactionCategories.map { it.name }
                            .contains(category.name),
                        onAddCategory = {
                            viewModel.addTransactionCategory(it)
                        },
                        onRemoveCategory = {
                            viewModel.removeTransactionCategory(it)
                        }
                    )
                }
            }
        }
    }
}
