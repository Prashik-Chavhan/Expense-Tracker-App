package com.example.expensetracker.core.util

import androidx.compose.ui.graphics.Color
import kotlin.random.Random

fun generateRandomColor(): Color {
    val random = Random.nextInt(0x1000000)
    return Color(random or 0xff000000.toInt())
}