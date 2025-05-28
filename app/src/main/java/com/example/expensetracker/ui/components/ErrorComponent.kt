package com.example.expensetracker.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle

@Composable
fun ErrorComponent(message:String) {
    Box(modifier = Modifier.fillMaxSize()){
        Text(
            text = message,
            modifier = Modifier.align(Alignment.Center),
            style = TextStyle(color = MaterialTheme.colorScheme.primary)

        )
    }

}