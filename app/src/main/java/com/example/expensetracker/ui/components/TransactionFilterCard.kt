package com.example.expensetracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FilterCard(
    modifier: Modifier = Modifier,
    filterName: String,
    isActive: Boolean,
    onClick: (String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(filterName) }
            .padding(horizontal = 8.dp)
    ) {
        Text(
            text = filterName,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        if (isActive) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .width(filterName.length.dp * 8)
                    .height(4.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
            )
        }
    }
}