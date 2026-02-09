package com.example.expense_tracking.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CheckboxDefaults.colors
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expense_tracking.ExpenseTrackingApplicationTheme
import com.example.expense_tracking.R

@Composable
fun ProfilePage(
    modifier: Modifier = Modifier,
    //viewModel: ExpenseTrackingViewModel = ExpenseTrackingViewModel(),
) {

    var selectedTab by remember { mutableStateOf("Profile") }

    val backgroundColor = Color(0xFFE6F0FA)

    Scaffold(
        containerColor = backgroundColor,
        bottomBar = {
            NavigationBar(
                containerColor = Color.White
            ) {
                NavigationBarItem(
                    icon = { Icon(painterResource(R.drawable.home_24dp_e3e3e3_fill0_wght400_grad0_opsz24), "Home") },
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
                    icon = { Icon(painterResource(R.drawable.add_circle_24dp_e3e3e3_fill0_wght400_grad0_opsz24), "Add") },
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
                    icon = { Icon(painterResource(R.drawable.photo_camera_24dp_e3e3e3_fill0_wght400_grad0_opsz24), "Scan") },
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
                    icon = { Icon(painterResource(R.drawable.account_balance_wallet_24dp_e3e3e3_fill0_wght400_grad0_opsz24), "Budget") },
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
                    icon = { Icon(painterResource(R.drawable.person_24dp_e3e3e3_fill0_wght400_grad0_opsz24), "Profile") },
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(text = stringResource(R.string.profile))
            Spacer(modifier = Modifier.padding(8.dp))
            Text(text = stringResource(R.string.profile_intro))
            Spacer(modifier = Modifier.padding(8.dp))

            Profilepage_Information(modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.padding(8.dp))
            Profilepage_AccountDetail(
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Profilepage_WalletDetail(
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Profilepage_Security(modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.padding(8.dp))
            Profilepage_Preferences(modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.padding(16.dp))

            Button(
                onClick = { /*viewModel.logout()*/ },
                modifier = Modifier.align(Alignment.CenterHorizontally).fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0x80FFFFFF )
                ),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(0.5.dp, Color(0xFFD32F2F))
            ) {
                Text(
                    text = stringResource(R.string.logout),
                    color = Color(0xFFD32F2F)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

    }
}


@Composable
fun Profilepage_Information(
    modifier: Modifier = Modifier,
    name: String = "John Doe",
    email: String = "john.doe@email.com",
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // profile pic later add
            Spacer(
                modifier = Modifier.padding(4.dp)
            )
            Text(text = name)
            Spacer(
                modifier = Modifier.padding(4.dp)
            )
            Text(text = email)
            Spacer(
                modifier = Modifier.padding(4.dp)
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
                Text(text = stringResource(R.string.edit_profile))
            }
        }
    }
}

@Composable
fun Profilepage_AccountDetail(
    username: String = "John Doe",
    email: String = "johndoe@example.com",
    phone: String = "+1 (555)123-4567",
    modifier: Modifier = Modifier
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
            Text(text = stringResource(R.string.account_detail))
            Spacer(
                modifier = Modifier.padding(4.dp)
            )
            Text(text = stringResource(R.string.fullname))
            Spacer(
                modifier = Modifier.padding(4.dp)
            )
            Card(
                modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF0F0F0)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = username,
                    modifier.padding(10.dp)
                )
            }
            Spacer(
                modifier = Modifier.padding(4.dp)
            )
            Text(text = stringResource(R.string.email))
            Spacer(
                modifier = Modifier.padding(4.dp)
            )
            Card(
                modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF0F0F0)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = email,
                    modifier.padding(10.dp)
                )
            }
            Spacer(
                modifier = Modifier.padding(4.dp)
            )
            Text(text = stringResource(R.string.phone_num))
            Spacer(
                modifier = Modifier.padding(4.dp)
            )
            Card(
                modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF0F0F0)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = phone,
                    modifier.padding(10.dp)
                )
            }
        }
    }
}

@Composable
fun Profilepage_WalletDetail(
    cash: String = "0.00",
    ewallet: String = "0.00",
    bank: String = "0.00",
    currency: String = "$",
    modifier: Modifier = Modifier
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
            Text(text = stringResource(R.string.wallet_detail))
            Spacer(
                modifier = Modifier.padding(4.dp)
            )
            Text(text = stringResource(R.string.currency))
            Spacer(
                modifier = Modifier.padding(4.dp)

            )
            Card(
                modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF4CAF50)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = currency
                    )
                }
            }
            Text(text = stringResource(R.string.cash))
            Spacer(
                modifier = Modifier.padding(4.dp)
            )
            Card(
                modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFE6F7E6)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                )  {
                    Text(
                        text = cash
                    )
                }
            }
            Spacer(
                modifier = Modifier.padding(4.dp)
            )
            Text(text = stringResource(R.string.bank))
            Spacer(
                modifier = Modifier.padding(4.dp)
            )
            Card(
                modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFE6F0FF)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                )  {
                    Text(
                        text = bank
                    )
                }
            }
            Spacer(
                modifier = Modifier.padding(4.dp)
            )
            Text(text = stringResource(R.string.ewallet))
            Spacer(
                modifier = Modifier.padding(4.dp)
            )
            Card(
                modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFF5E6)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                )  {
                    Text(
                        text = ewallet
                    )
                }
            }
            Spacer(
                modifier = Modifier.padding(4.dp)
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
                Text(text = stringResource(R.string.edit_budget))
            }
        }
    }
}

@Composable
fun Profilepage_Security(
    modifier: Modifier = Modifier
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
            Text(text = stringResource(R.string.security))
            Spacer(
                modifier = Modifier.padding(4.dp)
            )
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0x80FFFFFF),
                    contentColor = Color.Black
                ),
                border = BorderStroke(0.5.dp, Color.Gray),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(text = stringResource(R.string.change_password))
            }
            Spacer(
                modifier = Modifier.padding(4.dp)
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
                Text(text = stringResource(R.string.change_email))
            }
        }
    }
}

@Composable
fun Profilepage_Preferences(
    modifier: Modifier = Modifier,
    Language: String = "English"
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
            Text(text = stringResource(R.string.preferences))
            Spacer(
                modifier = Modifier.padding(4.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // icons
                Column {
                    Text(text = stringResource(R.string.push_notification))
                    Text(text = stringResource(R.string.push_notigication_intro), fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = true,
                    onCheckedChange = { /*TODO*/ },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color.Black,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color.Black
                    )
                )
            }
            Spacer(
                modifier = Modifier.padding(4.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // icons
                Column {
                    Text(text = stringResource(R.string.language))
                    Text(text = Language, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFF1565C0)
                    )
                ) {
                    Text(text = stringResource(R.string.change_language))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    ExpenseTrackingApplicationTheme {
        ProfilePage()
    }
}