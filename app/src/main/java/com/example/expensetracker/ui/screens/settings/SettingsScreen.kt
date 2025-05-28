package com.example.expensetracker.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.expensetracker.ui.alertDialogs.ThemeDialog
import com.example.expensetracker.ui.components.CustomTopAppBar
import com.example.expensetracker.ui.components.SettingsRow

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsScreenViewModel = hiltViewModel(),
    onAboutClicked: () -> Unit
) {
    val theme by viewModel.theme.collectAsStateWithLifecycle()
    val uiState by viewModel.settingsScreenUiState.collectAsStateWithLifecycle()
    SettingsScreenContent(
        theme = theme,
        uiState = uiState,
        onThemeChanged = { viewModel.updateTheme(it) },
        onThemeDialogClicked = viewModel::toggleThemeDialogVisibility,
        onAboutClicked = { onAboutClicked() },
        modifier = modifier
    )
}

@Composable
fun SettingsScreenContent(
    theme: String,
    uiState: SettingsScreenUiState,
    onAboutClicked: () -> Unit,
    onThemeDialogClicked: () -> Unit,
    onThemeChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = "Settings",
                canNavigateBack = false,
                onNavBackClicked = {}
            )
        }
    ) { paddingValues ->
        if (uiState.isThemeDialogVisible) {
            ThemeDialog(
                onThemeChanged = { theme -> onThemeChanged(theme) },
                onDialogDismiss = { onThemeDialogClicked() },
                currentTheme = theme
            )
        }
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                SettingsRow(
                    title = "About",
                    onItemClicked = { onAboutClicked() }
                )
            }
            item {
                SettingsRow(
                    title = "Dark Theme",
                    onItemClicked = { onThemeDialogClicked() }
                )
            }
        }
    }
}

@Preview
@Composable
fun SettingsScreenContentPreview() {

}