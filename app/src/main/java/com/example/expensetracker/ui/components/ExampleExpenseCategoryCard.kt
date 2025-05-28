package com.example.expensetracker.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.expensetracker.core.util.ExampleExpenseCategory

@Composable
fun ExampleExpenseCategoryCard(
    category: ExampleExpenseCategory,
    isSelected: Boolean,
    onAddCategory: (ExampleExpenseCategory) -> Unit,
    onRemoveCategory: (ExampleExpenseCategory) -> Unit,
) {
    InputChip(
        modifier = Modifier.padding(horizontal = 6.dp),
        selected = isSelected,
        onClick = {
            if (isSelected) {
                onRemoveCategory(category)
            } else {
                onAddCategory(category)
            }
        },
        label = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = category.name,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Spacer(modifier = Modifier.width(5.dp))

                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = "Category Selected",
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
        }
    )
}