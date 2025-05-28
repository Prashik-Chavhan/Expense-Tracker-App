package com.example.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.expensetracker.ui.navigation.AppNavigation
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: MainViewModel = hiltViewModel()
            val theme = viewModel.theme.collectAsStateWithLifecycle()
            val shouldShowOnboarding = runBlocking { viewModel.shouldShowOnboarding.first() }
            ExpenseTrackerTheme(
                themeMode = theme.value
            ) {
                AppNavigation(
                    shouldShowOnBoarding = shouldShowOnboarding
                )
            }
        }
    }
}