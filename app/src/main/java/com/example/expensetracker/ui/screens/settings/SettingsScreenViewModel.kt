package com.example.expensetracker.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.core.util.Constants
import com.example.expensetracker.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsScreenUiState(
    val isThemeDialogVisible:Boolean = false
)
@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,


) : ViewModel(){

    private val _settingsScreenUiState = MutableStateFlow(SettingsScreenUiState())
    val settingsScreenUiState = _settingsScreenUiState.asStateFlow()

    val theme = userPreferencesRepository.getTheme()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = Constants.DARK_MODE
        )

    fun updateTheme(themeValue: String) {
        viewModelScope.launch {
            userPreferencesRepository.setTheme(themeValue = themeValue)

        }
    }

    fun toggleThemeDialogVisibility(){
        val initialState = _settingsScreenUiState.value.isThemeDialogVisible
        _settingsScreenUiState.update {
            it.copy(isThemeDialogVisible = !initialState)
        }
    }
}