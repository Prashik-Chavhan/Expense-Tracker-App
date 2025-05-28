package com.example.expensetracker.ui.bottomsheets.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.expensetracker.core.util.DateUtil
import com.example.expensetracker.core.util.UiEvent
import com.example.expensetracker.core.util.convertTimeMillisToLocalDate
import com.example.expensetracker.core.util.getFormattedTime
import com.example.expensetracker.core.util.getNumericInitialValue
import com.example.expensetracker.core.util.toast
import com.example.expensetracker.domain.models.TransactionCategory
import com.example.expensetracker.domain.Mappers.toExternalModel
import com.example.expensetracker.ui.bottomsheets.viewModels.AddTransactionFormState
import com.example.expensetracker.ui.bottomsheets.viewModels.AddTransactionScreenViewModel
import com.example.expensetracker.ui.components.MenuSample
import com.example.expensetracker.ui.components.TimePickerDialog
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate
import java.time.LocalTime
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun AddTransactionBottomSheet(
    onClicked: () -> Unit,
    viewModel: AddTransactionScreenViewModel = hiltViewModel()
){
    val transactionCategories = viewModel.transactionCategories
        .collectAsStateWithLifecycle(initialValue = emptyList())
        .value
        .map { it.toExternalModel() }
    val context = LocalContext.current
    val formState = viewModel.formState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = true){
        viewModel.eventFlow.collectLatest {
            when(it){
                is UiEvent.ShowSnackBar -> {
                    context.toast(msg = it.uiText)
                }
                else -> {}
            }
        }
    }

    AddTransactionBottomSheetContent(
        transactionCategories = transactionCategories,
        formState = formState.value,
        onChangeTransactionName = { viewModel.onChangeTransactionName(it) },
        onChangeTransactionAmount = { viewModel.onChangeTransactionAmount(it) },
        onChangeTransactionCategory ={ viewModel.onChangeSelectedTransactionCategory(it) } ,
        onChangeTransactionTime = { viewModel.onChangeTransactionTime(it) },
        onChangeTransactionDate = { viewModel.onChangeTransactionDate(it) },
        addTransaction = {
            viewModel.addTransaction()
            onClicked()
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionBottomSheetContent(
    transactionCategories:List<TransactionCategory>,
    formState:AddTransactionFormState,
    onChangeTransactionName:(String) -> Unit,
    onChangeTransactionAmount:(String) -> Unit,
    onChangeTransactionCategory:(TransactionCategory) -> Unit,
    onChangeTransactionTime:(LocalTime) -> Unit,
    onChangeTransactionDate:(LocalDate) -> Unit,
    addTransaction:() -> Unit
) {
    val keyBoard = LocalSoftwareKeyboardController.current
    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    val selectedDate = remember { mutableStateOf<Long?>(null) }
    val selectedHour = remember { mutableIntStateOf(0) }
    val selectedMinute = remember { mutableIntStateOf(0) }
    val timePickerState = rememberTimePickerState()

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                        selectedDate.value = datePickerState.selectedDateMillis
                    }
                ) {
                    Text(
                        text = "Confirm",
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        ) {
            DatePicker(datePickerState)
        }
    }

    if (showTimePicker) {
        TimePickerDialog(
            onDismiss = { showTimePicker = false },
            onConfirm = {
                selectedHour.intValue = timePickerState.hour
                selectedMinute.intValue = timePickerState.minute
                showTimePicker = false
            }
        ) {
            TimePicker(state = timePickerState)
        }
    }
    val getFormattedTime = remember(selectedHour.intValue, selectedMinute.intValue) {
        val calender = Calendar.getInstance()
        calender.set(Calendar.HOUR_OF_DAY, selectedHour.intValue)
        calender.set(Calendar.MINUTE, selectedMinute.intValue)
        SimpleDateFormat("hh:mm a", Locale.getDefault()).format(calender.time)
    }

    LaunchedEffect(
        key1 = datePickerState.selectedDateMillis,
        key2 = listOf(selectedHour.intValue, selectedMinute.intValue)
    ) {
        datePickerState.selectedDateMillis?.let {
            onChangeTransactionDate(convertTimeMillisToLocalDate(it))
        }
//        onChangeTransactionTime(convertFormattedToNanoTime(getFormattedTime))
        onChangeTransactionTime(getFormattedTime(selectedHour = selectedHour.intValue, selectedMinute = selectedMinute.intValue))
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
    ) {
        Column(
            modifier = Modifier
//                .background(color = MaterialTheme.colorScheme.onBackground)
//                .fillMaxSize()
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
                    text = "Create Transaction",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center
                )
            }
            OutlinedTextField(
                value = formState.transactionName,
                onValueChange = {
                    onChangeTransactionName(it)
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Transaction Name",
                    )
                }
            )
            Spacer(modifier = Modifier.size(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                OutlinedTextField(
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    value = getNumericInitialValue(formState.transactionAmount),
                    onValueChange = {
                        onChangeTransactionAmount(it)
                    },
                    modifier = Modifier.fillMaxWidth(0.5f),
                    placeholder = {
                        Text(
                            text = "Transaction Amount",
                        )
                    }
                )
                Spacer(modifier = Modifier.size(16.dp))

                val currentIndex =
                    if (formState.transactionCategory == null) 0
                    else
                        transactionCategories
                            .map { it.transactionCategoryName }
                            .indexOf(formState.transactionCategory.transactionCategoryName)

                MenuSample(
                    menuWidth = 300,
                    selectedIndex = currentIndex,
                    menuItems = transactionCategories.map { it.transactionCategoryName },
                    onSelectedIndexChanged = { index ->
                        val selectedTransactionCategory = transactionCategories[index]
                        onChangeTransactionCategory(selectedTransactionCategory)
                    }
                )
            }
            Spacer(modifier = Modifier.size(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(100.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly,
                ) {
                    val selectedLocalDate = selectedDate.value?.let { convertTimeMillisToLocalDate(it) }
                    val formattedDate = selectedLocalDate?.let { DateUtil.generateFormatDate(it) }
                    Text(
                        text = formattedDate ?: DateUtil.generateFormatDate(formState.transactionDate),
                        modifier = Modifier.fillMaxWidth(0.5f),
                    )
                    Button(
                        modifier = Modifier
                            .width(width = 150.dp),
                        onClick = { showDatePicker = true }
                    ) {
                        Text(
                            text = "Pick A date",
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly,
                ) {

                    Text(
                        text = getFormattedTime ?: formState.transactionTime.toString(),
                        modifier = Modifier.fillMaxWidth(0.5f),
                    )
                    Button(
                        modifier = Modifier.width(150.dp),
                        onClick = { showTimePicker = true }
                    ) {
                        Text(
                            text = "Pick A Time",
                        )
                    }
                }
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                onClick = {
                    keyBoard?.hide()
                    addTransaction()
                }
            ) {
                Text(
                    text = "Save Transaction",
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}