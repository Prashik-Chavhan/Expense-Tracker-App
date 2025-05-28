package com.example.expensetracker.ui.bottomsheets.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.expensetracker.core.util.UiEvent
import com.example.expensetracker.core.util.toast
import com.example.expensetracker.ui.bottomsheets.viewModels.AddExpenseCategoryScreenViewModel
import com.example.expensetracker.ui.bottomsheets.viewModels.AddExpenseCategoryUiState
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AddExpenseCategoryBottomSheet(
    onClicked: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddExpenseCategoryScreenViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest {
            when (it) {
                is UiEvent.ShowSnackBar -> {
                    context.toast(msg = it.uiText)
                }

                else -> {}
            }
        }
    }
    AddExpenseCategoryBottomSheetContent(
        uiState = uiState.value,
        onChangeExpenseCategoryName = {
            viewModel.onChangeExpenseName(it)
        },
        addExpenseCategory = {
            viewModel.addExpenseCategory()
            onClicked()
        }
    )
}


@Composable
fun AddExpenseCategoryBottomSheetContent(
    uiState: AddExpenseCategoryUiState,
    onChangeExpenseCategoryName: (String) -> Unit,
    addExpenseCategory: () -> Unit,
    modifier: Modifier = Modifier
) {
    val keyBoard = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Create Expense Category",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
        OutlinedTextField(
            value = uiState.expenseCategoryName,
            onValueChange = {
                onChangeExpenseCategoryName(it)
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = "Expense Category Name"
                )
            }
        )
        Spacer(modifier = Modifier.size(16.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (!uiState.isLoading) {
                    keyBoard?.hide()
                    addExpenseCategory()
                }
            },
            enabled = uiState.expenseCategoryName.isNotEmpty()
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                Text(
                    text = "Add"
                )
            }
        }
    }
}