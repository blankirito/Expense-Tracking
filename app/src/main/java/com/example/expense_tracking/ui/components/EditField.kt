package com.example.expense_tracking.ui.components

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.expense_tracking.R
import java.util.Calendar
import com.example.expense_tracking.data.Constants.*
import com.example.expense_tracking.viewmodel.AddExpenseViewModel


@Composable
fun EditNameField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField (
        value = value,
        singleLine = true,
        modifier = modifier,
        onValueChange = onValueChange,
        label = { Text(stringResource(R.string.example_name)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )
}

@Composable
fun EditEmailField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField (
        value = value,
        singleLine = true,
        modifier = modifier,
        onValueChange = onValueChange,
        label = { Text(stringResource(R.string.youexamplecom)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
    )
}

@Composable
fun EditPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var passwordVisible by remember { mutableStateOf(false) }

    TextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        modifier = modifier,
        label = { Text(stringResource(R.string.passwordInput)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val image = if (passwordVisible)
                painterResource(id = R.drawable.visibility_24dp_e3e3e3_fill0_wght400_grad0_opsz24) // 你自己放个眼睛图标
            else
                painterResource(id = R.drawable.visibility_off_24dp_e3e3e3_fill0_wght400_grad0_opsz24)

            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(painter = image, contentDescription = if (passwordVisible) "Hide password" else "Show password")
            }
        }
    )
}


@Composable
fun Editamountfield(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        singleLine = true,
        modifier = modifier,
        onValueChange = onValueChange,
        label = { Text(stringResource(R.string.example_amount)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCategoryField(
    viewModel: AddExpenseViewModel,
    modifier: Modifier = Modifier,
    options: List<String> = CategoryConstants.CATEGORIES
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        TextField(
            value = viewModel.category,
            onValueChange = { /* only for reading */ },
            readOnly = true,
            label = { Text("Category") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        viewModel.onCategoryChange(option)
                        expanded = false
                    }
                )
            }
        }
    }
}




@Composable
fun Editdatefield(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val calendar = Calendar.getInstance()

    val year: Int
    val month: Int
    val day: Int

    val parts = value.split("-")
    if (parts.size == 3) {
        year = parts[0].toIntOrNull() ?: calendar.get(Calendar.YEAR)
        month = (parts[1].toIntOrNull() ?: (calendar.get(Calendar.MONTH) + 1)) - 1 // Calendar月份0起
        day = parts[2].toIntOrNull() ?: calendar.get(Calendar.DAY_OF_MONTH)
    } else {
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)
    }

    val datePickerDialog = remember {
        DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
            val formatted = "%04d-%02d-%02d".format(selectedYear, selectedMonth + 1, selectedDay)
            onValueChange(formatted)
        }, year, month, day)
    }

    TextField(
        value = value,
        onValueChange = { onValueChange(it) },
        singleLine = true,
        modifier = modifier,
        label = { Text(stringResource(R.string.example_date)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        trailingIcon = {
            IconButton(onClick = { datePickerDialog.show() }) {
                Icon(
                    painter = painterResource(id = R.drawable.calendar_today_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
                    contentDescription = "Select Date"
                )
            }
        }
    )
}

@Composable
fun Editdescriptionfield(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        singleLine = true,
        modifier = modifier,
        onValueChange = onValueChange,
        label = { Text(stringResource(R.string.example_description)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )
}

@Composable
fun EditCurrencyfield(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        singleLine = true,
        modifier = modifier,
        onValueChange = onValueChange,
        label = { Text(stringResource(R.string.example_currency)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )
}

@Composable
fun EditPhoneField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = value,
        singleLine = true,
        modifier = Modifier,
        onValueChange = onValueChange ,
        label = { Text(stringResource(R.string.example_phone_num)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalletDropDown(
    modifier: Modifier = Modifier,
    selectedOption: String,
    onSelected: (String) -> Unit
) {
    val options = listOf(
        "account_cash_001" to "Cash",
        "account_bank_001" to "Bank",
        "account_ewallet_001" to "E-Wallet"
    )
    var expanded by remember { mutableStateOf(false) }

    val selectedName = options.find { it.first == selectedOption }?.second ?: ""

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {

        TextField(
            value = selectedName,
            onValueChange = {},
            readOnly = true,
            label = { Text("Account") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { (id, name) ->
                DropdownMenuItem(
                    text = { Text(name) },
                    onClick = {
                        onSelected(id)
                        expanded = false
                    }
                )
            }
        }
    }
}

