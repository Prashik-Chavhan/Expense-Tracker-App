package com.example.expensetracker.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun GraphFilterCard(
    modifier: Modifier = Modifier,
    filterName: String,
    isActive: Boolean,
    onClick: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(6.dp)
            .clickable { onClick(filterName) }
    ) {
//        if (isActive) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .border(
                        width = if (isActive) 1.dp else 0.dp,
                        color = if (isActive) MaterialTheme.colorScheme.onBackground else Color.Transparent,
                        shape = RoundedCornerShape(5.dp),
                    )
                    .padding(6.dp)

            ) {
                Text(
                    text = filterName,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
//        } else {
//            Row(
//                horizontalArrangement = Arrangement.Center,
//                verticalAlignment = Alignment.CenterVertically,
//                modifier = modifier
//                    .padding(6.dp)
//
//            ) {
//                Text(
//                    text = filterName,
//                    fontWeight = FontWeight.SemiBold,
//                    fontSize = 14.sp,
//                    style = TextStyle(color = MaterialTheme.colorScheme.primary)
//                )
//            }
//        }
    }
}