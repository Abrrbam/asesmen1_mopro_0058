package com.abrahamputra0058.asesment1.ui.screen

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.ExposedDropdownMenuDefaults.textFieldColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.abrahamputra0058.asesment1.R
import com.abrahamputra0058.asesment1.navigation.Screen
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


//Note: Terakhir saat run untuk remember selectedTime, aplikasi sudah crash. Aplikasi crash saat klik FAB...

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailAgendaScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}){
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                title = {
                    Text(text = stringResource(id = R.string.add_agenda))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = {navController.navigate(Screen.About.route)}) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription =  stringResource(R.string.about_app),
                            tint = MaterialTheme.colorScheme.primary

                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        FormDetailAgenda(Modifier.padding(innerPadding)) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormDetailAgenda(modifier: Modifier = Modifier) {
//    Implicit intent
    val context = LocalContext.current

    //  Pembuatan Dropdown
    val options = listOf(
        stringResource(R.string.course),
        stringResource(R.string.task),
        stringResource(R.string.other)
    )


    var judul by rememberSaveable { mutableStateOf("") }
    var judulError by remember { mutableStateOf(false) }

    var selectedDate by rememberSaveable { mutableStateOf<Long?>(null) }
    var dateError by remember { mutableStateOf(false) }

    var deskripsi by rememberSaveable { mutableStateOf("") }
    var deskripsiError by remember { mutableStateOf(false) }

    var selectedTime by rememberSaveable { mutableStateOf("") }

    var selectedOptionText by rememberSaveable { mutableStateOf(options[0]) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.intro_add),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )

//        Dropdown tipe agenda
        DropdownTypeAgenda(options = options, selectedOptionText = selectedOptionText, onOptionSelected = { selectedOptionText = it})

//        Judul agenda
        OutlinedTextField(
            value = judul,
            onValueChange = { judul = it },
            label = { Text(text = stringResource(R.string.title_agenda)) },
            isError = judulError,
            trailingIcon = { IconPicker(judulError) },
            supportingText = { ErrorHint(judulError) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

//        Pilih tanggal
        DatePickerFieldToModal(selectedDate = selectedDate, onDateChange = {
            selectedDate = it
            dateError = false
        },
            isError = dateError)

//      Time picker
        SimpleTimeInput(onConfirm = {
            timePickerState ->
            selectedTime = "${timePickerState.hour}:${timePickerState.minute}"
        }, onDismiss = {})

//      Deskripsi
        OutlinedTextField(
            value = deskripsi,
            onValueChange = { deskripsi = it },
            label = { Text(text = stringResource(R.string.description)) },
            placeholder = { Text(text = stringResource(R.string.ph_description)) },
            isError = deskripsiError,
            trailingIcon = { IconPicker(deskripsiError) },
            supportingText = { ErrorHint(deskripsiError) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth(), maxLines = 5
        )

//        Button Note
        Button(
            onClick = {
                judulError = (judul == "" || judul == "0")
                deskripsiError = (deskripsi == "" || deskripsi == "0")
                dateError = (selectedDate == null)

//                if (judulError || deskripsiError || dateError) return@Button
            },
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 4.dp)
        ) {
            Text(text = stringResource(R.string.note))
        }

        if (!judulError && !deskripsiError){
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp), thickness = 1.dp
            )

            Image(
                painter = painterResource(R.drawable.other),
                contentScale = ContentScale.FillBounds,
                contentDescription = stringResource(R.string.image_name)
            )
//            Tampilkan tanggal, waktu, tipe agenda, judul, dan deskripsi/detail
            Text(
                text = stringResource(R.string.date_time_note, convertMillisToDate(selectedDate ?: System.currentTimeMillis()),
                    selectedTime),
                style = MaterialTheme.typography.headlineMedium
            )
            Text(text = stringResource(R.string.type_note, selectedOptionText),
                style = MaterialTheme.typography.labelLarge)
            Text(text = stringResource(R.string.title_note, judul),
                style = MaterialTheme.typography.bodyMedium)
            Text(text = stringResource(R.string.description_note, deskripsi),
                style = MaterialTheme.typography.bodyMedium)

//            Share/ Implicit intent
            Button(
                onClick = {
                    shareAgenda(
                        context = context,
                        message = context.getString(R.string.template_share, convertMillisToDate(selectedDate ?: System.currentTimeMillis()), selectedTime, selectedOptionText, judul, deskripsi)
                    )
                },
                modifier = Modifier.padding(8.dp),
                contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
            ) {
                Text(text = stringResource(R.string.share))
            }
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownTypeAgenda(
    options: List<String>,
    selectedOptionText: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selectedOptionText,
            readOnly = true,
            label = { Text(text = stringResource(id = R.string.type_agenda)) },
            onValueChange = {},
            trailingIcon = { TrailingIcon(expanded = expanded) },
            colors = textFieldColors(),
            modifier = Modifier
                .menuAnchor(type = MenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(text = selectionOption) },
                    onClick = {
                        onOptionSelected(selectionOption)
                        expanded = false
                    }
                )
            }
        }
    }
}


//Date picker
@Composable
fun DatePickerFieldToModal(
    modifier: Modifier = Modifier,
    isError: Boolean,
    selectedDate: Long?,
    onDateChange: (Long?) -> Unit = {}) {

    var showModal by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = selectedDate?.let { convertMillisToDate(it) } ?: "",
        onValueChange = { },
        label = { Text(text = stringResource(R.string.date_agenda)) },
        isError = isError,
        placeholder = { Text("MM/DD/YYYY") },
        trailingIcon = {
            Icon(Icons.Default.DateRange, contentDescription = "Select date")
        },
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(selectedDate) {
                awaitEachGesture {
                    // Modifier.clickable doesn't work for text fields, so we use Modifier.pointerInput
                    // in the Initial pass to observe events before the text field consumes them
                    // in the Main pass.
                    awaitFirstDown(pass = PointerEventPass.Initial)
                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                    if (upEvent != null) {
                        showModal = true
                    }
                }
            }
    )

    if (showModal) {
        DatePickerModal(
            onDateSelected = { onDateChange(it) },
            onDismiss = {
                showModal = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

//Time picker
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleTimeInput(
    onConfirm: (TimePickerState) -> Unit,
    onDismiss: () -> Unit,
) {
    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )

    Column(modifier = Modifier.padding(16.dp)) {
        TimeInput(
            state = timePickerState,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(onClick = onDismiss, modifier = Modifier.padding(end = 8.dp)) {
                Text("Cancel")
            }
            Button(onClick = { onConfirm(timePickerState) }, modifier = Modifier.padding(start = 8.dp)) {
                Text("OK")
            }
        }
    }
}

private fun shareAgenda(context: Context, message: String){
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }
    if (shareIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(shareIntent)
    }
}

@Composable
fun IconPicker(isError: Boolean) {
    if (isError)
        Icon(
            imageVector = Icons.Filled.Warning, contentDescription = null
        )
}

@Composable
fun ErrorHint(isError: Boolean) {
    if (isError)
        Text(text = stringResource(R.string.input_invalid))
}

