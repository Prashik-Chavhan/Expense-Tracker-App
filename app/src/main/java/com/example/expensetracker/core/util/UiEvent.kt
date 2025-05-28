package com.example.expensetracker.core.util

import com.example.expensetracker.ui.screens.home.BottomSheets


abstract class Event

sealed class UiEvent: Event() {
    data class ShowSnackBar(val uiText: String) : UiEvent()
    data class Navigate(val route: String) : UiEvent()
    data class OpenBottomSheet(val bottomSheet: BottomSheets) : UiEvent()
}