package com.example.expensetracker.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.expensetracker.core.util.truncate


@Composable
fun MenuSample(
    label: String = "",
    selectedIndex: Int,
    onSelectedIndexChanged: (Int) -> Unit,
    menuItems: List<String>,
    menuWidth: Int
) {
    var isMenuListExpanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .width(menuWidth.dp)
            .height(95.dp)
            .padding(10.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        if (label.isNotEmpty()) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        ComposeMenu(
            menuItems = menuItems,
            isMenuExpanded = isMenuListExpanded,
            selectedIndex = selectedIndex,
            onMenuExpandChange = {
                isMenuListExpanded = true
            },
            onMenuDismiss = {
                isMenuListExpanded = false
            },
            onMenuItemClicked = { index ->
                onSelectedIndexChanged(index)
                isMenuListExpanded = false
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposeMenu(
    isMenuExpanded: Boolean,
    onMenuExpandChange: () -> Unit,
    onMenuDismiss: () -> Unit,
    menuItems: List<String>,
    selectedIndex: Int,
    onMenuItemClicked: (Int) -> Unit,
) {
    ExposedDropdownMenuBox(
        expanded = isMenuExpanded,
        onExpandedChange = { onMenuExpandChange() }
    ) {
        OutlinedTextField(
            value = if (menuItems.isEmpty()) "" else truncate(menuItems[selectedIndex], 15),
            onValueChange = {},
            modifier = Modifier.menuAnchor(
                type = MenuAnchorType.PrimaryEditable,
                enabled = true
            ),
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = isMenuExpanded
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
                focusedTextColor = MaterialTheme.colorScheme.onBackground
            ),
            textStyle = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.W500)
        )
        ExposedDropdownMenu(
            containerColor = MaterialTheme.colorScheme.background,
            tonalElevation = 8.dp,
            expanded = isMenuExpanded,
            onDismissRequest = { onMenuDismiss() }
        ) {
            menuItems.forEachIndexed { index, selectedOption ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = selectedOption,
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (selectedIndex == index) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onBackground
                        )
                    },
                    onClick = {
                        onMenuItemClicked(index)
                    }
                )
            }
        }
    }
}