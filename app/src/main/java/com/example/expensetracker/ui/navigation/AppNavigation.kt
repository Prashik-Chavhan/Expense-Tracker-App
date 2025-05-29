package com.example.expensetracker.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.core.util.Screens
import com.example.expensetracker.ui.screens.about.AboutScreen
import com.example.expensetracker.ui.screens.expense.ExpenseScreen
import com.example.expensetracker.ui.screens.expenses.AllExpensesScreen
import com.example.expensetracker.ui.screens.income.IncomeScreen
import com.example.expensetracker.ui.screens.incomes.AllIncomeScreen
import com.example.expensetracker.ui.screens.onboarding.OnboardingScreen
import com.example.expensetracker.ui.screens.transaction.TransactionScreen
import com.example.expensetracker.ui.screens.transactions.AllTransactionsScreen

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalAnimationApi::class
)
@Composable
fun AppNavigation(
    shouldShowOnBoarding: Boolean,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val startDestination =
        if (shouldShowOnBoarding) Screens.ONBOARDING_SCREEN else Screens.BOTTOM_TAB_NAVIGATION

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        val enterAnimation = scaleInEnterTransition()
        val exitAnimation = scaleOutExitTransition()
        val popEnterAnimation = scaleInPopEnterTransition()
        val popExitAnimation = scaleOutPopExitTransition()
        composable(Screens.ONBOARDING_SCREEN) {
            OnboardingScreen(
                onFinishClicked = {
                    navController.navigate(Screens.BOTTOM_TAB_NAVIGATION) {
                        launchSingleTop = true
                        popUpTo(0) {
                            saveState = false
                        }
                    }
                }
            )
        }
        composable(route = Screens.BOTTOM_TAB_NAVIGATION) {
            BottomNavigationWrapper(
                onAllIncomeClicked = { navController.navigate(Screens.ALL_INCOME_SCREEN) },
                onIncomeCardClicked = { incomeId -> navController.navigate(Screens.INCOME_SCREEN + "/$incomeId") },
                onAllTransactionsClicked = { navController.navigate(Screens.ALL_TRANSACTIONS_SCREEN) },
                onTransactionCardClicked = { transactionId -> navController.navigate(Screens.TRANSACTIONS_SCREEN + "/$transactionId") },
                onAllExpensesClicked = { navController.navigate(Screens.ALL_EXPENSES_SCREEN) },
                onExpenseCardClicked = { expenseId -> navController.navigate(Screens.EXPENSE_SCREEN + "/$expenseId") },
                onAboutClicked = { navController.navigate(Screens.ABOUT_SCREEN) }
            )
        }
        composable(
            route = Screens.ALL_INCOME_SCREEN,
            enterTransition = { enterAnimation },
            exitTransition = { exitAnimation },
            popEnterTransition = { popEnterAnimation },
            popExitTransition = { popExitAnimation }
        ) {
            AllIncomeScreen(
                onNavBackClicked = { navController.navigateUp() },
                onIncomeClicked = { incomeId -> navController.navigate(Screens.INCOME_SCREEN + "/$incomeId") }
            )
        }

        composable(
            route = Screens.INCOME_SCREEN + "/{id}",
            enterTransition = { enterAnimation },
            exitTransition = { exitAnimation },
            popEnterTransition = { popEnterAnimation },
            popExitTransition = { popExitAnimation }
        ) {
            IncomeScreen(
                onNavBackClicked = {
                    navController.navigateUp()
                }
            )
        }

        composable(
            route = Screens.TRANSACTIONS_SCREEN + "/{id}",
            enterTransition = { enterAnimation },
            exitTransition = { exitAnimation },
            popEnterTransition = { popEnterAnimation },
            popExitTransition = { popExitAnimation }
        ) {
            TransactionScreen(
                onNavBackClicked = {
                    navController.navigateUp()
                }
            )
        }
        composable(
            route = Screens.ALL_TRANSACTIONS_SCREEN,
            enterTransition = { enterAnimation },
            exitTransition = { exitAnimation },
            popEnterTransition = { popEnterAnimation },
            popExitTransition = { popExitAnimation }
        ) {
            AllTransactionsScreen(
                onTransactionCardClicked = { transactionId ->
                    navController.navigate(Screens.TRANSACTIONS_SCREEN + "/$transactionId")
                },
                onNavBackClicked = { navController.navigateUp() }
            )
        }

        composable(
            route = Screens.ALL_EXPENSES_SCREEN,
            enterTransition = { enterAnimation },
            exitTransition = { exitAnimation },
            popEnterTransition = { popEnterAnimation },
            popExitTransition = { popExitAnimation }
        ) {
            AllExpensesScreen(
                navigateToExpenseScreen = { navController.navigate(Screens.EXPENSE_SCREEN) },
                onNavBackClicked = { navController.navigateUp() }
            )
        }
        composable(
            route = Screens.EXPENSE_SCREEN + "/{id}",
            enterTransition = { enterAnimation },
            exitTransition = { exitAnimation },
            popEnterTransition = { popEnterAnimation },
            popExitTransition = { popExitAnimation }
        ) {
            ExpenseScreen(
                onNavBackClicked = { navController.navigateUp() }
            )
        }

        composable(
            route = Screens.ABOUT_SCREEN,
            enterTransition = { enterAnimation },
            exitTransition = { exitAnimation },
            popEnterTransition = { popEnterAnimation },
            popExitTransition = { popExitAnimation }
        ) {
            AboutScreen(
                onNavBackClicked = { navController.navigateUp() }
            )
        }
    }
}