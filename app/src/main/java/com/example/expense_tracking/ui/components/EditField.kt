package com.example.expense_tracking.ui.components

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.expense_tracking.R
import java.util.Calendar
import com.example.expense_tracking.data.Constants.*
import com.example.expense_tracking.data.model.Account
import com.example.expense_tracking.viewmodel.AddExpenseViewModel
import com.example.expense_tracking.viewmodel.ScanViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.collections.map


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
        onValueChange = { input ->
            val number = input.toDoubleOrNull()
            if (number == null) {
                if (input.isEmpty()) onValueChange("")
            } else if (number >= 0) {
                onValueChange(input)
            } else {}
        },
        label = { Text(stringResource(R.string.example_amount)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCategoryField(
    viewModel: AddExpenseViewModel,
    modifier: Modifier = Modifier,
    firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    val context = LocalContext.current
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return

    var expanded by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }
    var newCategoryName by remember { mutableStateOf("") }
    var selectedIcon by remember { mutableStateOf(CategoryConstants.FOOD) }

    val options = CategoryConstants.CATEGORIES + "Add New Category"

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        TextField(
            value = viewModel.category,
            onValueChange = { },
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
                        expanded = false
                        if (option == "Add New Category") {
                            showAddDialog = true
                        } else {
                            viewModel.onCategoryChange(option)
                        }
                    }
                )
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add New Category") },
            text = {
                Column {
                    TextField(
                        value = newCategoryName,
                        onValueChange = { newCategoryName = it },
                        label = { Text("Category Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Choose Icon:")
                    Spacer(modifier = Modifier.height(8.dp))

                    val icons = CategoryConstants.CATEGORY_ICONS.keys.toList()
                    val chunkedIcons = icons.chunked(4)
                    Column {
                        chunkedIcons.forEach { rowIcons ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                rowIcons.forEach { key ->
                                    val iconRes = CategoryConstants.CATEGORY_ICONS[key]!!
                                    IconButton(
                                        onClick = { selectedIcon = key },
                                        modifier = Modifier.size(48.dp)
                                            .then(
                                                if (selectedIcon == key) Modifier.border(2.dp, Color.Blue, RoundedCornerShape(8.dp))
                                                else Modifier
                                            )
                                    ) {
                                        Icon(painterResource(iconRes), contentDescription = key, tint = Color(0xFF64B5F6))
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (newCategoryName.isNotBlank()) {
                        val iconResId = CategoryConstants.CATEGORY_ICONS[selectedIcon]
                            ?: CategoryConstants.CATEGORY_ICONS[CategoryConstants.OTHERS]!!

                        // ⚡ 新增 category 插入到第一个
                        if (!CategoryConstants.CATEGORIES.contains(newCategoryName)) {
                            CategoryConstants.CATEGORIES.add(0, newCategoryName)
                            CategoryConstants.CATEGORY_ICONS[newCategoryName] = iconResId
                            // ⚡ 用户自定义 category map
                            CategoryConstants.USER_CATEGORY_ICONS[newCategoryName] = selectedIcon
                        }

                        val data = mapOf(
                            "userId" to currentUserId,
                            "name" to newCategoryName,
                            "icon" to selectedIcon
                        )
                        firestore.collection("categories")
                            .add(data)
                            .addOnSuccessListener {
                                viewModel.onCategoryChange(newCategoryName)
                                showAddDialog = false
                                newCategoryName = ""
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "Error adding category", Toast.LENGTH_SHORT).show()
                            }
                    }
                }) { Text("Save") }
            },
            dismissButton = { Button(onClick = { showAddDialog = false }) { Text("Cancel") } }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanCategoryField(
    viewModel: ScanViewModel,
    modifier: Modifier = Modifier,
    firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    val context = LocalContext.current
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return

    var expanded by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }
    var newCategoryName by remember { mutableStateOf("") }
    var selectedIcon by remember { mutableStateOf(CategoryConstants.FOOD) }

    val options = CategoryConstants.CATEGORIES + "Add New Category"

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier.fillMaxWidth()
    ) {
        TextField(
            value = viewModel.category,
            onValueChange = { },
            readOnly = true,
            label = { Text("Category") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(Color.White)
                .padding(4.dp)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        expanded = false
                        if (option == "Add New Category") {
                            showAddDialog = true
                        } else {
                            viewModel.onCategoryChange(option)
                        }
                    }
                )
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add New Category") },
            text = {
                Column {
                    TextField(
                        value = newCategoryName,
                        onValueChange = { newCategoryName = it },
                        label = { Text("Category Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Choose Icon:")
                    Spacer(modifier = Modifier.height(8.dp))

                    val icons = CategoryConstants.CATEGORY_ICONS.keys.toList()
                    val chunkedIcons = icons.chunked(4)
                    Column {
                        chunkedIcons.forEach { rowIcons ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                rowIcons.forEach { key ->
                                    val iconRes = CategoryConstants.CATEGORY_ICONS[key]!!
                                    IconButton(
                                        onClick = { selectedIcon = key },
                                        modifier = Modifier.size(48.dp)
                                            .then(
                                                if (selectedIcon == key) Modifier.border(2.dp, Color.Blue, RoundedCornerShape(8.dp))
                                                else Modifier
                                            )
                                    ) {
                                        Icon(painterResource(iconRes), contentDescription = key, tint = Color(0xFF64B5F6))
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (newCategoryName.isNotBlank()) {
                        val iconResId = CategoryConstants.CATEGORY_ICONS[selectedIcon]
                            ?: CategoryConstants.CATEGORY_ICONS[CategoryConstants.OTHERS]!!

                        // ⚡ 新增 category 插入到第一个
                        if (!CategoryConstants.CATEGORIES.contains(newCategoryName)) {
                            CategoryConstants.CATEGORIES.add(0, newCategoryName)
                            CategoryConstants.CATEGORY_ICONS[newCategoryName] = iconResId
                            // ⚡ 用户自定义 category map
                            CategoryConstants.USER_CATEGORY_ICONS[newCategoryName] = selectedIcon
                        }

                        val data = mapOf(
                            "userId" to currentUserId,
                            "name" to newCategoryName,
                            "icon" to selectedIcon
                        )
                        firestore.collection("categories")
                            .add(data)
                            .addOnSuccessListener {
                                viewModel.onCategoryChange(newCategoryName)
                                showAddDialog = false
                                newCategoryName = ""
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "Error adding category", Toast.LENGTH_SHORT).show()
                            }
                    }
                }) { Text("Save") }
            },
            dismissButton = { Button(onClick = { showAddDialog = false }) { Text("Cancel") } }
        )
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

@Composable
fun EditProfileDialog(
    currentName: String,
    currentPhone: String,
    onConfirm: (String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(currentName) }
    var phone by remember { mutableStateOf(currentPhone) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Profile") },
        text = {
            Column {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(name, phone) },
                enabled = name.isNotBlank() && phone.isNotBlank()
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}


@Composable
fun ChangePasswordDialog(
    onConfirm: (String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Change Password") },
        text = {
            Column {
                TextField(
                    value = oldPassword,
                    onValueChange = { oldPassword = it },
                    label = { Text("Old Password") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("New Password") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm New Password") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (newPassword == confirmPassword) {
                        onConfirm(oldPassword, newPassword)
                    }
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
