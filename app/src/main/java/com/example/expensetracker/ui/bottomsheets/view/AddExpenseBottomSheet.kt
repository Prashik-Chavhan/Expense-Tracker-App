package com.example.expensetracker.ui.bottomsheets.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.expensetracker.core.util.UiEvent
import com.example.expensetracker.core.util.getNumericInitialValue
import com.example.expensetracker.core.util.toast
import com.example.expensetracker.domain.Mappers.toExternalModel
import com.example.expensetracker.domain.models.ExpenseCategory
import com.example.expensetracker.ui.bottomsheets.viewModels.AddExpenseFormState
import com.example.expensetracker.ui.bottomsheets.viewModels.AddExpenseScreenViewModel
import com.example.expensetracker.ui.components.MenuSample
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AddExpenseBottomSheet(
    onClicked: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddExpenseScreenViewModel = hiltViewModel()
) {
    val expenseCategories = viewModel.expenseCategories
        .collectAsStateWithLifecycle()
        .value
        .map { it.toExternalModel() }

    val formState = viewModel.uiState.collectAsStateWithLifecycle()
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

    AddExpenseBottomSheetContent(
        expenseCategories = expenseCategories,
        formState = formState.value,
        onChangeExpenseName = {
            viewModel.onChangeExpenseName(it)
        },
        onChangeExpenseAmount = {
            viewModel.onChangeExpenseAmount(it)
        },
        onChangeExpenseCategory = {
            viewModel.onChangeSelectedExpenseCategory(it)
        },
        addExpense = {
            viewModel.addExpense()
            onClicked()
        }
    )
}

@Composable
fun AddExpenseBottomSheetContent(
    expenseCategories: List<ExpenseCategory>,
    formState: AddExpenseFormState,
    onChangeExpenseName: (String) -> Unit,
    onChangeExpenseAmount: (String) -> Unit,
    onChangeExpenseCategory: (ExpenseCategory) -> Unit,
    addExpense: () -> Unit,
    modifier: Modifier = Modifier
) {
    val keyBoard = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
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
                text = "Create Expense ",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                textAlign = TextAlign.Center
            )
        }
        OutlinedTextField(
            value = formState.expenseName,
            onValueChange = {
                onChangeExpenseName(it)
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = "Expense Name"
                )
            }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,

            ) {
            OutlinedTextField(
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                value = getNumericInitialValue(formState.expenseAmount),
                onValueChange = {
                    onChangeExpenseAmount(it)
                },
                modifier = Modifier.fillMaxWidth(0.5f),
                placeholder = {
                    Text(
                        text = "Expense Amount"
                    )
                }
            )
            Spacer(modifier = Modifier.width(16.dp))
            val currentIndex =
                if (formState.selectedExpenseCategory == null) 0
                else
                    expenseCategories.map { it.expenseCategoryName }
                        .indexOf(formState.selectedExpenseCategory.expenseCategoryName)
            MenuSample(
                menuWidth = 300,
                selectedIndex = currentIndex,
                menuItems = expenseCategories.map { it.expenseCategoryName },
                onSelectedIndexChanged = { index ->
                    val selectedExpenseCategory = expenseCategories[index]
                    onChangeExpenseCategory(selectedExpenseCategory)
                }
            )
        }
        if (expenseCategories.isEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Enter an expense category to be add to an expense",
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            onClick = {
                keyBoard?.hide()
                addExpense()
            }
        ) {
            Text(
                text = "Save"
            )
        }
    }
}