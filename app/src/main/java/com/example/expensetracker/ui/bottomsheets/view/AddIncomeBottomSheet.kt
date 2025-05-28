package com.example.expensetracker.ui.bottomsheets.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.expensetracker.core.util.UiEvent
import com.example.expensetracker.core.util.getNumericInitialValue
import com.example.expensetracker.core.util.toast
import com.example.expensetracker.ui.bottomsheets.viewModels.AddIncomeFormState
import com.example.expensetracker.ui.bottomsheets.viewModels.AddIncomeScreenViewModel
import kotlinx.coroutines.flow.collectLatest

@Preview
@Composable
fun AddIncomeBottomSheetPreview() {
    AddIncomeBottomSheetContent(
        formState = AddIncomeFormState(),
        onChangeIncomeName = { },
        onChangeIncomeAmount = { },
        addIncome = { }
    )
}

@Composable
fun AddIncomeBottomSheet(
    onAddIncome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddIncomeScreenViewModel = hiltViewModel(),
) {
    val formState = viewModel.formState.collectAsStateWithLifecycle()

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

    AddIncomeBottomSheetContent(
        formState = formState.value,
        onChangeIncomeName = {
            viewModel.onChangeIncomeName(text = it)
        },
        onChangeIncomeAmount = {
            viewModel.onChangeIncomeAmount(text = it)
        },
        addIncome = {
            viewModel.addIncome()
            onAddIncome()
        }
    )
}


@Composable
fun AddIncomeBottomSheetContent(
    formState: AddIncomeFormState,
    onChangeIncomeName: (String) -> Unit,
    onChangeIncomeAmount: (String) -> Unit,
    addIncome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val keyBoard = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier
            .padding(10.dp),
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
                text = "Add Income",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                textAlign = TextAlign.Center
            )
        }
        OutlinedTextField(
            value = formState.incomeName,
            onValueChange = {
                onChangeIncomeName(it)
            },
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = {
                Text(
                    text = "Income Name"
                )
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            value = getNumericInitialValue(formState.incomeAmount),
            onValueChange = {
                onChangeIncomeAmount(it)
            },
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = {
                Text(
                    text = "Income Amount"
                )
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            onClick = {
                keyBoard?.hide()
                addIncome()
            }
        ) {
            Text(
                text = "Save Income",
                fontWeight = FontWeight.Bold
            )
        }
    }
}