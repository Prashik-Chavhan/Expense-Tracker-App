package com.example.expensetracker.ui.bottomsheets.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
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
import com.example.expensetracker.ui.bottomsheets.viewModels.AddTransactionCategoryFormState
import com.example.expensetracker.ui.bottomsheets.viewModels.AddTransactionCategoryScreenViewModel
import kotlinx.coroutines.flow.collectLatest


@Composable
fun AddTransactionCategoryBottomSheet(
    onClicked: () -> Unit,
    viewModel: AddTransactionCategoryScreenViewModel = hiltViewModel()
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

    AddTransactionCategoryBottomSheetContent(
        formState = formState.value,
        onChangeTransactionCategoryName = { viewModel.onChangeTransactionCategoryName(it) },
        addTransactionCategory = {
            viewModel.addTransactionCategory()
            onClicked()
        }
    )
}


@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddTransactionCategoryBottomSheetContent(
    formState: AddTransactionCategoryFormState,
    onChangeTransactionCategoryName: (String) -> Unit,
    addTransactionCategory: () -> Unit
) {
    val keyBoard = LocalSoftwareKeyboardController.current
    Column(
        modifier = Modifier
            .fillMaxSize()
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
                text = "Create Transaction Category",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                textAlign = TextAlign.Center
            )
        }
        OutlinedTextField(
            value = formState.transactionCategoryName,
            onValueChange = {
                onChangeTransactionCategoryName(it)
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = {
                Text(
                    text = "Transaction Category Name"
                )
            }
        )
        Button(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {
                keyBoard?.hide()
                addTransactionCategory()

            }
        ) {
            Text(
                text = "Save",
            )
        }
    }
}