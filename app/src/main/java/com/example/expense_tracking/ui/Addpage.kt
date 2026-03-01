package com.example.expense_tracking.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expense_tracking.ExpenseTrackingApplicationTheme
import com.example.expense_tracking.R
import com.example.expense_tracking.data.model.*
import com.example.expense_tracking.ui.components.EditCurrencyfield
import com.example.expense_tracking.ui.components.Editamountfield
import com.example.expense_tracking.ui.components.Editdatefield
import com.example.expense_tracking.ui.components.Editdescriptionfield
import com.example.expense_tracking.ui.components.*
import com.example.expense_tracking.viewmodel.AddExpenseViewModel
import com.example.expense_tracking.viewmodel.BudgetViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar

@Composable
fun AddPage(
    modifier: Modifier = Modifier,
    viewModel: AddExpenseViewModel = viewModel()
) {
    var selectedTab by remember { mutableStateOf("Add") }

    val backgroundColor = Color(0xFFE6F0FA)

    var currency by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Scaffold(
        containerColor = backgroundColor,
        bottomBar = {
            NavigationBar(
                containerColor = Color.White
            ) {
                NavigationBarItem(
                    icon = { Icon(painter = painterResource(R.drawable.home_24dp_e3e3e3_fill0_wght400_grad0_opsz24), contentDescription = "Home") },
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
                    icon = { Icon(painter = painterResource(R.drawable.add_circle_24dp_e3e3e3_fill0_wght400_grad0_opsz24), contentDescription = "Add") },
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
                    icon = { Icon(painter = painterResource(R.drawable.photo_camera_24dp_e3e3e3_fill0_wght400_grad0_opsz24), contentDescription = "Scan") },
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
                    icon = { Icon(painter = painterResource(R.drawable.account_balance_wallet_24dp_e3e3e3_fill0_wght400_grad0_opsz24), contentDescription = "Budget") },
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
                    icon = { Icon(painter = painterResource(R.drawable.person_24dp_e3e3e3_fill0_wght400_grad0_opsz24), contentDescription = "Profile") },
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.add_expense),
                fontSize = 25.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.add_expense_intro),
                color = Color(0xFF424242)
            )
            Spacer(modifier = Modifier.height(12.dp))

            Addpage_ExpenseDetail(
                viewModel = viewModel,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Addpage_tip(modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun Addpage_ExpenseDetail(
    viewModel: AddExpenseViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.expense_detail),
                fontSize = 18.sp
            )
            Spacer(
                modifier = Modifier.height(12.dp)
            )
            Text(
                text = stringResource(R.string.currency),
                fontWeight = Bold
            )
            EditCurrencyfield(
                value = viewModel.currency,
                onValueChange = { viewModel.onCurrencyChange(it) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(
                modifier = Modifier.height(16.dp)
            )
            Text(
                text = stringResource(R.string.wallet),
                fontWeight = Bold
            )
            Spacer(
                modifier = Modifier.height(12.dp)
            )
            WalletDropDown(
                modifier = Modifier.fillMaxWidth(),
                selectedOption = viewModel.selectedAccountId,
                onSelected = {
                    viewModel.onAccountSelected(it)
                }
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )
            Text(
                text = stringResource(R.string.amount),
                fontWeight = Bold
            )
            Editamountfield(
                value = viewModel.amount,
                onValueChange = { viewModel.onAmountChange(it) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(
                modifier = Modifier.height(16.dp)
            )
            Text(
                text = stringResource(R.string.category),
                fontWeight = Bold
            )
            EditCategoryField(
                viewModel = viewModel,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(
                modifier = Modifier.height(16.dp)
            )
            Text(
                text = stringResource(R.string.date),
                fontWeight = Bold
            )
            Editdatefield(
                value = viewModel.date,
                onValueChange = { viewModel.onDateChange(it) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(
                modifier = Modifier.height(16.dp)
            )
            Text(
                text = stringResource(R.string.description),
                fontWeight = Bold
            )
            Editdescriptionfield(
                value = viewModel.description,
                onValueChange = { viewModel.onDescriptionChange(it) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(
                modifier = Modifier.height(16.dp)
            )
            Button(
                onClick = {
                    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return@Button

                    try {
                        val parts = viewModel.date.split("-")
                        if (parts.size == 3) {

                            viewModel.saveExpense(
                                userId = currentUserId,
                                onSuccess = {
                                    Toast.makeText(context, "Expense saved successfully", Toast.LENGTH_SHORT).show()
                                    BudgetViewModel().loadUserData(currentUserId)
                                },
                                onError = { e ->
                                    Toast.makeText(context, "Error saving expense: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                            )
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Invalid date format", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1565C0),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.save_expense)
                )
                Spacer(
                    modifier = Modifier.width(8.dp)
                )
            }
        }
    }
}

@Composable
fun Addpage_tip(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFD0E2F8),
            contentColor = Color(0xFF1E4EA0)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.tip),
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddPreviewSimple() {
    ExpenseTrackingApplicationTheme {
        AddPage()
    }
}
