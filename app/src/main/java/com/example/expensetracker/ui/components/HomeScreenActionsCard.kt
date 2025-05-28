package com.example.expensetracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun HomeScreenCardActionsPreview() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HomeScreenActionsCard(
            name = "Add Income",
            icon = Icons.Default.Add,
            onClick = {

            }
        )
        HomeScreenActionsCard(
            name = "Add Transaction",
            icon = Icons.Default.Add,
            onClick = {

            }
        )
        HomeScreenActionsCard(
            name = "Add Expense",
            icon = Icons.Default.Add,
            onClick = {

            }
        )
        HomeScreenActionsCard(
            name = "Add Transaction Category",
            icon = Icons.Default.Add,
            onClick = {

            }
        )
        HomeScreenActionsCard(
            name = "Add Expense Category",
            icon = Icons.Default.Add,
            onClick = {

            }
        )
    }

}

@Composable
fun HomeScreenActionsGrid(onClick: (Int) -> Unit) {
    val actions = listOf(
        "Add Income" to Icons.Default.AttachMoney,
        "Add Transaction" to Icons.AutoMirrored.Filled.List,
        "Add Expense" to Icons.Default.MoneyOff,
        "Add Transaction Category" to Icons.Default.Category,
        "Add Expense Category" to Icons.AutoMirrored.Filled.Label
    )
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier
            .fillMaxWidth()
            .height(295.dp),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(actions) { index, (name, icon) ->
            HomeScreenActionsCard(
                name = name,
                icon = icon,
                onClick = { onClick(index) }
            )
        }
    }
}


@Composable
fun HomeScreenActionsCard(
    name: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(100.dp)
            .height(145.dp)
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        IconButton(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            onClick = onClick
        ) {
            Icon(
                modifier = Modifier.size(30.dp),
                imageVector = icon,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                contentDescription = name
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = name,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}