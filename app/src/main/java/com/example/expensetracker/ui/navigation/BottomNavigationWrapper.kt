package com.example.expensetracker.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.R
import com.example.expensetracker.core.util.Screens
import com.example.expensetracker.domain.models.BottomNavItem
import com.example.expensetracker.ui.screens.analytics.AnalyticsScreen
import com.example.expensetracker.ui.screens.home.HomeScreen
import com.example.expensetracker.ui.screens.search.SearchScreen
import com.example.expensetracker.ui.screens.settings.SettingsScreen


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun BottomNavigationWrapper(
    onAllIncomeClicked: () -> Unit,
    onIncomeCardClicked: (String) -> Unit,
    onAllTransactionsClicked: () -> Unit,
    onTransactionCardClicked: (String) -> Unit,
    onAllExpensesClicked: () -> Unit,
    onExpenseCardClicked: (String) -> Unit,
    onAboutClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavBar(
                items = listOf(
                    BottomNavItem(
                        name = stringResource(id = R.string.home),
                        route = Screens.HOME_SCREEN,
                        selectedIcon = Icons.Default.Home,
                        unselectedIcon = Icons.Outlined.Home,
                    ),
                    BottomNavItem(
                        name = stringResource(id = R.string.search),
                        route = Screens.SEARCH_SCREEN,
                        selectedIcon = Icons.Default.Search,
                        unselectedIcon = Icons.Outlined.Search
                    ),
                    BottomNavItem(
                        name = stringResource(id = R.string.analytics),
                        route = Screens.ANALYTICS_SCREEN,
                        selectedIcon = Icons.Default.BarChart,
                        unselectedIcon = Icons.Outlined.BarChart
                    ),
                    BottomNavItem(
                        name = stringResource(id = R.string.settings),
                        route = Screens.SETTINGS_SCREEN,
                        selectedIcon = Icons.Default.Settings,
                        unselectedIcon = Icons.Outlined.Settings
                    ),
                ),
                navController = navController,
                onItemClick = {
                    navController.navigate(it.route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = false
                        }
                    }
                }
            )
        }
    ) {
        BottomNavigation(
            navController = navController,
            onAllIncomeClicked = { onAllIncomeClicked() },
            onIncomeCardClicked = { incomeId -> onIncomeCardClicked(incomeId) },
            onAllTransactionsClicked = { onAllTransactionsClicked() },
            onTransactionCardClicked = { transactionId -> onTransactionCardClicked(transactionId) },
            onAllExpensesClicked = { onAllExpensesClicked() },
            onExpenseCardClicked = { expenseId -> onExpenseCardClicked(expenseId) },
            onAboutClicked = { onAboutClicked() },
            modifier = modifier,
        )
    }
}

@ExperimentalMaterial3Api
@Composable
fun BottomNavBar(
    items: List<BottomNavItem>,
    navController: NavController,
    onItemClick: (BottomNavItem) -> Unit
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry.value?.destination?.route
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.tertiary,
    ) {
        items.forEachIndexed { index, item ->
            val isSelected = item.route == currentRoute
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.name,
                        tint = if (isSelected) Color.Black else Color.White
                    )
                },
                label = {
                    Text(
                        text = item.name,
                        color = MaterialTheme.colorScheme.onTertiary
                    )
                },
                selected = isSelected,
                onClick = { onItemClick(item) }
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
@Composable
fun BottomNavigation(
    navController: NavHostController,
    onAllIncomeClicked: () -> Unit,
    onIncomeCardClicked: (String) -> Unit,
    onAllTransactionsClicked: () -> Unit,
    onTransactionCardClicked: (String) -> Unit,
    onAllExpensesClicked: () -> Unit,
    onExpenseCardClicked: (String) -> Unit,
    onAboutClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screens.HOME_SCREEN
    ) {
        val enterAnimation = scaleInEnterTransition()
        val exitAnimation = scaleOutExitTransition()
        val popEnterAnimation = scaleInPopEnterTransition()
        val popExitAnimation = scaleOutPopExitTransition()
        composable(
            route = Screens.HOME_SCREEN,
            enterTransition = { enterAnimation },
            exitTransition = { exitAnimation },
            popEnterTransition = { popEnterAnimation },
            popExitTransition = { popExitAnimation }
        ) {
            HomeScreen(
                onAllIncomeClicked = { onAllIncomeClicked() },
                onIncomeCardClicked = { incomeId -> onIncomeCardClicked(incomeId) },
                onAllTransactionsClicked = { onAllTransactionsClicked() },
                onTransactionCardClicked = { transactionId -> onTransactionCardClicked(transactionId) },
                onAllExpensesClicked = { onAllExpensesClicked() },
                onExpenseCardClicked = { expenseId -> onExpenseCardClicked(expenseId) }
            )
        }
        composable(
            route = Screens.SEARCH_SCREEN,
            enterTransition = { enterAnimation },
            exitTransition = { exitAnimation },
            popEnterTransition = { popEnterAnimation },
            popExitTransition = { popExitAnimation }
        ) {
            SearchScreen(
                onTransactionCardClicked = { transactionId -> onTransactionCardClicked(transactionId) }
            )
        }
        composable(
            route = Screens.ANALYTICS_SCREEN,
            enterTransition = { enterAnimation },
            exitTransition = { exitAnimation },
            popEnterTransition = { popEnterAnimation },
            popExitTransition = { popExitAnimation }
        ) {
            AnalyticsScreen(
                onTransactionCardClicked = { transactionId -> onTransactionCardClicked(transactionId) }
            )
        }
        composable(
            route = Screens.SETTINGS_SCREEN,
            enterTransition = { enterAnimation },
            exitTransition = { exitAnimation },
            popEnterTransition = { popEnterAnimation },
            popExitTransition = { popExitAnimation }
        ) {
            SettingsScreen(
                onAboutClicked = { onAboutClicked() }
            )
        }
    }
}