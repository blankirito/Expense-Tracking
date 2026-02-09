package com.example.expense_tracking.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expense_tracking.ExpenseTrackingApplicationTheme
import com.example.expense_tracking.R
import com.example.expense_tracking.ui.components.EditCurrencyfield
import com.example.expense_tracking.ui.components.Editamountfield
import com.example.expense_tracking.ui.components.Editcategoryfield
import com.example.expense_tracking.ui.components.Editdatefield
import com.example.expense_tracking.ui.components.Editdescriptionfield

@Composable
fun AddPage(modifier: Modifier = Modifier) {

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
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(text = stringResource(R.string.add_expense))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = stringResource(R.string.add_expense_intro))
            Spacer(modifier = Modifier.height(12.dp))

            Addpage_ExpenseDetail(
                currency = currency,
                onCurrencyChange = { currency = it },
                amount = amount,
                onAmountChange = { amount = it },
                category = category,
                onCategoryChange = { category = it },
                date = date,
                onDateChange = { date = it },
                description = description,
                onDescriptionChange = { description = it }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Addpage_tip(modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun Addpage_ExpenseDetail(
    currency: String,
    onCurrencyChange: (String) -> Unit,
    amount: String,
    onAmountChange: (String) -> Unit,
    category: String,
    onCategoryChange: (String) -> Unit,
    date: String,
    onDateChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
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
                text = stringResource(R.string.expense_detail)
            )
            Spacer(
                modifier = Modifier.height(8.dp)
            )
            Text(
                text = stringResource(R.string.currency)
            )
            EditCurrencyfield(
                value = currency,
                onValueChange = onCurrencyChange,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(
                modifier = Modifier.height(8.dp)
            )
            Text(
                text = stringResource(R.string.amount)
            )
            // dropdown menu for account (cash, wallet, bank)
            Editamountfield(
                value = amount,
                onValueChange = onAmountChange,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(
                modifier = Modifier.height(8.dp)
            )
            Text(
                text = stringResource(R.string.category)
            )
            Editcategoryfield(
                value = category,
                onValueChange = onCategoryChange,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(
                modifier = Modifier.height(8.dp)
            )
            Text(
                text = stringResource(R.string.date)
            )
            Editdatefield(
                value = date,
                onValueChange = onDateChange,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(
                modifier = Modifier.height(8.dp)
            )
            Text(
                text = stringResource(R.string.description)
            )
            Editdescriptionfield(
                value = description,
                onValueChange = onDescriptionChange,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(
                modifier = Modifier.height(12.dp)
            )
            Button(
                onClick = { /*TODO*/ },
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
