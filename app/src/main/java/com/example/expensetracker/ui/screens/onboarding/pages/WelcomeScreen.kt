package com.example.expensetracker.ui.screens.onboarding.pages

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensetracker.R
import com.example.expensetracker.ui.components.OnBoardingCard
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = Modifier
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Welcome to My Expense Tracker App",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 42.sp,
                color = MaterialTheme.colorScheme.onBackground,
                lineHeight = 40.sp,
                textAlign = TextAlign.Start
            )
            OnBoardingCard(
                header = "The perfect expense tracker app",
                description = "Expense Tracker App is an app that allows you to track your income , daily transactions and expenses ",
                image = R.drawable.heart
            )
            OnBoardingCard(
                header = "Search Transactions ",
                description = "You can search transactions from a certain period ",
                image = R.drawable.search
            )
            OnBoardingCard(
                header = "Visualize Expenditure",
                description = "Expense Tracker App visualizes your data in bar and pie charts for your insights",
                image = R.drawable.analytics
            )
        }
    }
}

@Preview(name = "Light Mode", showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Preview() {
//    ExpenseTrackerTheme {
//        WelcomeScreen()
//    }
}