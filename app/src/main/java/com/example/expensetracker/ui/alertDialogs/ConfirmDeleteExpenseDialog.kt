package com.example.expensetracker.ui.alertDialogs

import android.content.res.Configuration
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.expensetracker.R
import com.example.expensetracker.domain.models.Expense
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme

@Composable
fun ConfirmDeleteDialog(
    closeDeleteDialog:() -> Unit,
    type: String,
    title: String,
    onDeleteClicked:() -> Unit,
) {

    val text = when(type) {
        "expense" -> stringResource(R.string.delete_expense_description)
        "transaction" -> stringResource(R.string.delete_transaction_description)
        "income" -> stringResource(R.string.delete_income_description)
        else -> ""
    }
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        titleContentColor = MaterialTheme.colorScheme.onBackground,
        textContentColor = MaterialTheme.colorScheme.onBackground,
        onDismissRequest = {
            closeDeleteDialog()
        },
        title = {
            Text(
                text = "${stringResource(id = R.string.delete)} $title",
            )
        },
        text = {
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDeleteClicked()
                }
            ) {
                Text(
                    text = stringResource(id = R.string.delete),
                    color = Color.Red
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    closeDeleteDialog()
                }
            ) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    )
}

@Preview(name = "Light mode", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Preview() {
//    ExpenseTrackerTheme {
//        ConfirmDeleteDialog(
//            onDeleteClicked = {},
//            type = "income",
//            title = "Rent",
//            closeDeleteDialog = {}
//        )
//    }
}