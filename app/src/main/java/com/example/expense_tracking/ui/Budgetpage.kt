package com.example.expense_tracking.ui

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expense_tracking.R
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expense_tracking.ExpenseTrackingApplicationTheme
import com.example.expense_tracking.viewmodel.BudgetViewModel

@Composable
fun BudgetPage(
    modifier: Modifier = Modifier,
    userId: String,
   viewModel: BudgetViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {

    LaunchedEffect(userId) {
        viewModel.loadUserData(userId)
    }

    val accounts by viewModel.accounts.collectAsState()
    val currency by viewModel.currency.collectAsState()
    val totalSpend by viewModel.totalMonthlySpend.collectAsState()
    val totalLimit by viewModel.totalLimit.collectAsState()

    var selectedTab by remember { mutableStateOf("Budget") }

    val backgroundColor = Color(0xFFE6F0FA)

    Scaffold(
        containerColor = backgroundColor,
        bottomBar = {
            NavigationBar(
                containerColor = Color.White
            ) {
                NavigationBarItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.home_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
                            contentDescription = "Home"
                        )
                    },
                    label = { Text("Home") },
                    selected = selectedTab == "Home",
                    onClick = { selectedTab = "Home" },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = Color.Black,
                        unselectedTextColor = Color.Gray
                    )
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.add_circle_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
                            contentDescription = "Add"
                        )
                    },
                    label = { Text("Add") },
                    selected = selectedTab == "Add",
                    onClick = { selectedTab = "Add" },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = Color.Black,
                        unselectedTextColor = Color.Gray
                    )
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.photo_camera_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
                            contentDescription = "Camera"
                        )
                    },
                    label = { Text("Scan") },
                    selected = selectedTab == "Scan",
                    onClick = { selectedTab = "Scan" },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = Color.Black,
                        unselectedTextColor = Color.Gray
                    )
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.account_balance_wallet_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
                            contentDescription = "Budget"
                        )
                    },
                    label = { Text("Budget") },
                    selected = selectedTab == "Budget",
                    onClick = { selectedTab = "Budget" },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = Color.Black,
                        unselectedTextColor = Color.Gray
                    )
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.person_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
                            contentDescription = "Profile"
                        )
                    },
                    label = { Text("Profile") },
                    selected = selectedTab == "Profile",
                    onClick = { selectedTab = "Profile" },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = Color.Black,
                        unselectedTextColor = Color.Gray
                    )
                )
            }
        }
    ) { innerPadding ->

        var showAddDialog by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.budget_tracker),
                fontSize = 25.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
            Spacer(
                modifier = Modifier.padding(4.dp)
            )
            Text(
                text = stringResource(R.string.budget_tracker_intro),
                color = Color(0xFF424242)
            )
            Spacer(
                modifier = Modifier.padding(8.dp)
            )
            Budgetpage_TotalMonthlyBudget(
                total_cost = totalSpend,
                TotalBudget = totalLimit,
                currency = currency
            )
            Spacer(
                modifier = Modifier.padding(8.dp)
            )
            Budgetpage_CategoryBudgets(
                viewModel = viewModel,
                currency = currency,
                modifier = Modifier
                    .fillMaxWidth()
            )/*
            Spacer(
                modifier = Modifier.padding(8.dp)
            )
            Button(
                onClick = { showAddDialog = true },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0x80FFFFFF),
                    contentColor = Color.Black
                ),
                border = BorderStroke(0.5.dp, Color.Gray)
            ) {
                Text(
                    text = stringResource(R.string.add_new_category)
                )
            }

            if (showAddDialog) {
                var newCategory by remember { mutableStateOf("") }
                var newLimit by remember { mutableStateOf("") }

                androidx.compose.material3.AlertDialog(
                    onDismissRequest = { showAddDialog = false },
                    title = { Text(text = "Add New Category") },
                    text = {
                        Column {
                            androidx.compose.material3.OutlinedTextField(
                                value = newCategory,
                                onValueChange = { newCategory = it },
                                label = { Text("Category Name") }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            androidx.compose.material3.OutlinedTextField(
                                value = newLimit,
                                onValueChange = { newLimit = it },
                                label = { Text("Budget Limit") },
                                singleLine = true
                            )
                        }
                    },
                    confirmButton = {
                        Button(onClick = {
                            val parsedLimit = newLimit.toDoubleOrNull() ?: 0.0
                            if (newCategory.isNotBlank() && parsedLimit > 0) {
                                viewModel.addNewCategory(newCategory, parsedLimit)
                                showAddDialog = false
                            }
                        }) {
                            Text("Add")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { showAddDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }*/
            Spacer(
                modifier = Modifier.padding(8.dp)
            )
            Budgetpage_Tip()
        }
    }
}

@Composable
fun Budgetpage_TotalMonthlyBudget(
    modifier: Modifier = Modifier,
    total_cost: Double,
    currency: String,
    TotalBudget: Double
){
    val percentage: Double = if (TotalBudget > 0) (total_cost / TotalBudget) * 100 else 0.0
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors( containerColor = Color(0xFF1565C0),
            contentColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row {
                Icon(
                    painter = painterResource(R.drawable.wallet_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
                    contentDescription = "null",
                    modifier = Modifier
                )
                Spacer(
                    modifier = Modifier.padding(4.dp)
                )
                Text(
                    text = stringResource(R.string.monthly_budget)
                )
            }
            Spacer( modifier = Modifier.padding(4.dp) )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row {
                        Text(
                            text = currency,
                            fontSize = 24.sp
                        )
                        Spacer(modifier = Modifier.padding(1.dp))
                        Text(
                            text = String.format("%.2f", total_cost),
                            fontSize = 24.sp
                        )
                    }
                    Row {
                        Text(text = "of ", fontSize = 12.sp)
                        Row {
                            Text(text = currency, fontSize = 14.sp)
                            Spacer(modifier = Modifier.padding(1.dp))
                            Text(text = String.format("%.2f", TotalBudget), fontSize = 14.sp)
                        }
                    }
                }
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Row {
                        Text(text = percentage.toInt().toString(), fontSize = 20.sp)
                        Text(text = "%", fontSize = 20.sp)
                    }
                    Text(text = "used", fontSize = 14.sp) }
            }
            Spacer( modifier = Modifier.padding(4.dp) )
            LinearProgressIndicator(
                progress = (percentage / 100f).toFloat(),
                modifier = Modifier.fillMaxWidth(),
                color = Color.Black,
                trackColor = Color.LightGray
            )
            Spacer( modifier = Modifier.padding(4.dp) )
        }
    }
}


@Composable
fun Budgetpage_CategoryBudgets(
    modifier: Modifier = Modifier,
    viewModel: BudgetViewModel,
    currency: String
) {
    val categorySpends by viewModel.categorySpends.collectAsState()
    val budgets by viewModel.budgets.collectAsState()

    val sortedCategories = categorySpends.map { (category, spend) ->
        val limit = budgets.find { it.category == category }?.limit ?: 0.0
        val percentage = if (limit > 0) (spend * 100 / limit).toInt() else 0
        Triple(category, spend, limit) to percentage
    }.sortedByDescending { it.second }

    Column(modifier = modifier) {
        sortedCategories.forEach { (data, _) ->
            val (category, spend, limit) = data
            val remaining = limit - spend
            val percentage = if (limit > 0) (spend * 100 / limit).toInt() else 0

            Budgetpage_CategoryBudgetItem(
                category = category,
                spend = spend,
                limit = limit,
                remaining = remaining,
                percentage = percentage,
                currency = currency,
                onEditCategory = { newCategory, newLimit ->
                    viewModel.updateCategory(category, newCategory, newLimit)
                }
            )
            Spacer(modifier = Modifier.padding(8.dp))
        }
    }
}


@Composable
fun Budgetpage_CategoryBudgetItem(
    category: String,
    spend: Double,
    limit: Double,
    remaining: Double,
    percentage: Int,
    currency: String,
    onEditCategory: (String, Double) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    val alertColor = when {
        percentage >= 90 -> Color.Red
        percentage >= 70 -> Color(0xFFFF9800) // Orange
        else -> Color(0xFF4CAF50) // Green
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = category,
                    fontSize = 20.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit category",
                    modifier = Modifier
                        .clickable { showDialog = true }
                )
            }
            Spacer(modifier = Modifier.padding(4.dp))
            Row {
                Text(
                    text = currency,
                    fontSize = 14.sp
                )
                Spacer(
                    modifier = Modifier.padding(1.dp)
                )
                Text(text = String.format("%.2f", spend), fontSize = 14.sp)
                Spacer(modifier = Modifier.padding(2.dp))
                Text(text = "/")
                Spacer(modifier = Modifier.padding(2.dp))
                Text(text = currency,
                    fontSize = 14.sp,
                    color = Color(0xFF424242))
                Spacer(
                    modifier = Modifier.padding(1.dp)
                )
                Text(text = String.format("%.2f", limit),
                    fontSize = 14.sp,
                    color = Color(0xFF424242)
                    )
            }
            Spacer(modifier = Modifier.padding(4.dp))
            LinearProgressIndicator(
                progress = percentage / 100f,
                modifier = Modifier.fillMaxWidth(),
                color = alertColor,
                trackColor = Color.LightGray
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$percentage% used",
                    fontSize = 12.sp,
                    color = alertColor,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(text = currency, fontSize = 12.sp)
                Spacer(
                    modifier = Modifier.padding(1.dp)
                )
                Text(text =  String.format("%.2f", remaining), fontSize = 12.sp)
                Text(text = " left", fontSize = 12.sp)
            }
            if (percentage >= 100) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "⚠ Budget exceeded",
                    color = Color.Red,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    if (showDialog) {
        var newCategory by remember { mutableStateOf(category) }
        var newLimit by remember { mutableStateOf(limit.toString())}

        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Edit Category") },
            text = {
                Column {
                    /*androidx.compose.material3.OutlinedTextField(
                        value = newCategory,
                        onValueChange = { newCategory = it },
                        label = { Text("Category Name") }
                    )*/
                    Spacer(modifier = Modifier.height(8.dp))
                    androidx.compose.material3.OutlinedTextField(
                        value = newLimit,
                        onValueChange = { input ->
                            newLimit = input.filter { it.isDigit() || it == '.' }
                        },
                        label = { Text("Budget Limit") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val parsedLimit = newLimit.toDoubleOrNull() ?: -1.0
                        if (parsedLimit <= 0) {
                            val context = null
                            Toast.makeText(context, "Budget limit must be positive", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        onEditCategory(newCategory, parsedLimit)
                        showDialog = false
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                Row {
                    /*Button(
                        onClick = {
                            onDeleteCategory(category)
                            showDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("Delete", color = Color.White)
                    }*/

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = { showDialog = false }
                    ) {
                        Text("Cancel")
                    }
                }
            }
        )
    }
}



@Composable
fun Budgetpage_Tip(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFD0E2F8),
            contentColor = Color(0xFF1E4EA0)
        )
    ) {
        Text(
            text = stringResource(R.string.budget_tips),
            modifier = Modifier.padding(16.dp),
            fontSize = 14.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BudgetPreview() {
    ExpenseTrackingApplicationTheme {
        BudgetPage(
            userId = "preview_user"
        )
    }
}