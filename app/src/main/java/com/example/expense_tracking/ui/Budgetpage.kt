package com.example.expense_tracking.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.example.expense_tracking.ExpenseTrackingApplicationTheme

@Composable
fun BudgetPage(
    modifier: Modifier = Modifier
) {

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

        Column(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.budget_tracker)
            )
            Spacer(
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = stringResource(R.string.budget_tracker_intro)
            )
            Spacer(
                modifier = Modifier.padding(8.dp)
            )
            Budgetpage_TotalMonthlyBudget(
            )
            Spacer(
                modifier = Modifier.padding(8.dp)
            )
            Budgetpage_CategoryBudget(
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(
                modifier = Modifier.padding(8.dp)
            )
            Button(
                onClick = { /*TODO*/ },
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
    total_cost: Int = 2830,
    currency: String = "$",
    TotalBudget: Int = 3200
) {
    val percentage =
        if (TotalBudget > 0) (total_cost * 100) / TotalBudget else 0

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1565C0),
            contentColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.monthly_budget)
            )
            Spacer(
                modifier = Modifier.padding(4.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row {
                        Text(text = currency)
                        Text(text = total_cost.toString())
                    }
                    Row {
                        Text(text = "of ")
                        Row {
                            Text(text = currency)
                            Text(text = TotalBudget.toString())
                        }
                    }
                }
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Row {
                        Text(text = percentage.toString())
                        Text(text = "%")
                    }
                    Text(text = "used")
                }
            }
            Spacer(
                modifier = Modifier.padding(4.dp)
            )
            LinearProgressIndicator(
                progress = percentage / 100f,
                modifier = Modifier
                    .fillMaxWidth(),
                color = Color.Black,
                trackColor = Color.LightGray
            )
            Spacer(
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

@Composable
fun Budgetpage_CategoryBudget(
    modifier: Modifier = Modifier,
    category: String = "Food & Dining",
    total_cost: Int = 800,
    currency: String = "$",
    total_cost_usage: Int = 650,
    total_cost_remaining: Int = 150,
) {
    val percentage =
        if (total_cost > 0) (total_cost_usage * 100) / total_cost else 0

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = category)
                // edit icons later need add
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit category"
                )
            }
            Spacer(
                modifier = Modifier.padding(4.dp)
            )
            Row {
                Text(text = currency)
                Text(text = total_cost_usage.toString())
                Spacer(modifier = Modifier.padding(4.dp))
                Text(text = "/")
                Spacer(modifier = Modifier.padding(4.dp))
                Text(text = currency)
                Text(text = total_cost.toString())
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = percentage.toString())
                Text(text = "% used")
                Spacer(modifier = Modifier.weight(1f))
                Text(text = currency)
                Text(text = total_cost_remaining.toString())
                Text(text = " left")
            }
            Spacer(modifier = Modifier.padding(4.dp))
            LinearProgressIndicator(
                progress = percentage / 100f,
                modifier = Modifier
                    .fillMaxWidth(),
                color = Color.Black,
                trackColor = Color.LightGray
            )
            Spacer(modifier = Modifier.padding(4.dp))
        }
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
        BudgetPage()
    }
}