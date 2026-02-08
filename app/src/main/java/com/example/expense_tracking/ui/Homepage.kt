package com.example.expense_tracking.ui

import androidx.annotation.ColorRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.expense_tracking.R
import com.example.expense_tracking.ExpenseTrackingApplicationTheme


@Composable
fun HomePage(modifier: Modifier = Modifier) {

    var selectedTab by remember { mutableStateOf("Home") }

    val backgroundColor = Color(0xFFE6F0FA)

    Scaffold(
        containerColor = backgroundColor,
        bottomBar = {
            NavigationBar(
                containerColor = Color.White
            ){
                NavigationBarItem(
                    icon = {
                        Icon(Icons.Filled.Home, contentDescription = "Home")
                    },
                    label = { Text("Home") },
                    selected = selectedTab == "Home",
                    onClick = { selectedTab = "Home" },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = Color.Black,
                        unselectedTextColor = Color.Gray,
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
                        unselectedTextColor = Color.Gray,
                    )
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.photo_camera_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
                            contentDescription = "Scan"
                        )
                    },
                    label = { Text("Scan") },
                    selected = selectedTab == "Scan",
                    onClick = { selectedTab = "Scan" },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = Color.Black,
                        unselectedTextColor = Color.Gray,
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
                        unselectedTextColor = Color.Gray,
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
                        unselectedTextColor = Color.Gray,
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
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text( //Welcome, ${viewModel.currentUsername ?: "User"}
                text = stringResource(R.string.welcome_back),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.homepage_intro),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Homepage_totalBalance(
                    modifier = Modifier
                        .weight(1f)
                        .height(85.dp)
                )
                Homepage_thisMonth(
                    modifier = Modifier
                        .weight(1f)
                        .height(85.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Homepage_MonthlyTrends(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Homepage_ExpenseSummary(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Homepage_SpendingPredictions(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Homepage_RecentTransactions(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun Homepage_totalBalance(
    modifier: Modifier = Modifier,
    currency: String = "$",
    amount: Int = 8450
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1565C0),
            contentColor = Color.White
        )
    ){
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row {
                Text(
                    text = stringResource(R.string.totalBalance)
                )
            }
            Row {
                Text(
                    text = currency
                )
                Text(
                    text = amount.toString()
                )
            }
        }
    }
}

@Composable
fun Homepage_thisMonth(
    modifier: Modifier = Modifier,
    currency: String = "$",
    amount: Int = 2900
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row {
                Text(
                    text = stringResource(R.string.thisMonth)
                )
            }
            Row {
                Text(text = currency)
                Text(text = amount.toString())
            }
        }
    }
}

@Composable
fun Homepage_MonthlyTrends(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Text(
            text = stringResource(R.string.monthlyTrend),
            modifier = modifier.padding(10.dp)
        )
    }
}

@Composable
fun Homepage_ExpenseSummary(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Text(
            text = stringResource(R.string.expenseSummary),
            modifier = modifier.padding(10.dp)
        )
    }
}

@Composable
fun Homepage_SpendingPredictions(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Text(
            text = stringResource(R.string.spendingPrediction),
            modifier = modifier.padding(10.dp)
        )
    }
}

@Composable
fun Homepage_RecentTransactions(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        ) {
            Text(
                text = stringResource(R.string.recentTransaction),
                modifier = modifier.padding(10.dp)
            )
            Spacer(
                modifier = Modifier.padding(8.dp)
            )
            Homepage_RecentTransaction_Record(
                modifier = Modifier.fillMaxWidth(),
                currency = "$",
                total_cost = 55.00,
                date = "2025-11-17",
                description = "KFC"
            )
            Homepage_RecentTransaction_Record(
                modifier = Modifier.fillMaxWidth(),
                currency = "$",
                total_cost = 12.00,
                date = "2025-11-17",
                description = "7-11"
            )
            Homepage_RecentTransaction_Record(
                modifier = Modifier.fillMaxWidth(),
                currency = "$",
                total_cost = 56.00,
                date = "2025-11-17",
                description = "McDonald's"
            )
            Homepage_RecentTransaction_Record(
                modifier = Modifier.fillMaxWidth(),
                currency = "$",
                total_cost = 12.50,
                date = "2025-11-18",
                description = "Starbucks"
            )
        }
    }
}

@Composable
fun Homepage_RecentTransaction_Record(
    modifier: Modifier = Modifier,
    currency: String,
    total_cost: Double,
    date: String,
    description: String
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // icons
            Column {
                Text(
                    text = description
                )
                Text(
                    text = date
                )
            }
            Spacer(modifier.weight(1f))
            Row {
                Text(
                    text = "-"
                )
                Text(
                    text = currency
                )
                Text(
                    text = total_cost.toString()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    ExpenseTrackingApplicationTheme {
        HomePage()
    }
}