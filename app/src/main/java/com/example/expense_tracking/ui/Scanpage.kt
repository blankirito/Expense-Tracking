package com.example.expense_tracking.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expense_tracking.ExpenseTrackingApplicationTheme
import com.example.expense_tracking.R

@Composable
fun ScanPage(
    modifier: Modifier = Modifier
) {

    var selectedTab by remember { mutableStateOf("Scan") }

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

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Row {
                // icons
                Column (
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.scan_receipt)
                    )
                    Spacer(
                        modifier = Modifier.padding(4.dp)
                    )
                    Text(
                        text = stringResource(R.string.scan_receipt_intro)
                    )
                    Spacer(
                        modifier = Modifier.padding(4.dp)
                    )
                    Scanpage_CameraPreview(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                    )
                    Spacer(
                        modifier = Modifier.padding(4.dp)
                    )
                    Button(
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth(),
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
                    Spacer(
                        modifier = Modifier.padding(4.dp)
                    )
                    Button(
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.upload_from_gallery)
                        )
                    }
                    Spacer(
                        modifier = Modifier.padding(4.dp)
                    )
                    Scanpage_tips(
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun Scanpage_CameraPreview(
    modifier: Modifier = Modifier,
) {
    Card (
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Text(
            text = "camera preview"
        )
    }
}

@Composable
fun Scanpage_tips(
    modifier: Modifier = Modifier
) {
    Card (
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
                text = stringResource(R.string.scan_tip_1),
                fontSize = 12.sp
            )
            Spacer(
                modifier = Modifier.padding(4.dp)
            )
            Text(
                text = stringResource(R.string.scan_tip_2),
                fontSize = 12.sp
            )
            Text(
                text = stringResource(R.string.scan_tip_3),
                fontSize = 12.sp
            )
            Text(
                text = stringResource(R.string.scan_tip_4),
                fontSize = 12.sp
            )
            Text(
                text = stringResource(R.string.scan_tip_5),
                fontSize = 12.sp
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun ScanPreview() {
    ExpenseTrackingApplicationTheme {
        ScanPage()
    }
}