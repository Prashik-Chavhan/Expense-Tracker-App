package com.example.expensetracker.domain.models

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val name:String,
    val route:String,
    val selectedIcon:ImageVector,
    val unselectedIcon:ImageVector,
    val badgeCount:Int = 0,
)