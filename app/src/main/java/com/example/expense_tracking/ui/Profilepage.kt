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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CheckboxDefaults.colors
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expense_tracking.ExpenseTrackingApplicationTheme
import com.example.expense_tracking.R
import com.example.expense_tracking.data.UiState.ProfileUiState
import com.example.expense_tracking.ui.components.ChangeEmailDialog
import com.example.expense_tracking.ui.components.ChangePasswordDialog
import com.example.expense_tracking.ui.components.EditProfileDialog
import com.example.expense_tracking.viewmodel.ProfileViewModel

@Composable
fun ProfilePage(
    modifier: Modifier = Modifier,
    userId: String,
    viewModel: ProfileViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {

    val uistate by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadProfile(userId)
    }

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
            Text(
                text = stringResource(R.string.profile),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text(text = stringResource(R.string.profile_intro), color = Color(0xFF424242))
            Spacer(modifier = Modifier.padding(8.dp))
            Profilepage_Information(
                uistate = uistate,
                viewModel = viewModel,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Profilepage_AccountDetail(
                username = uistate.name,
                email = uistate.email,
                phone = uistate.phone,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Profilepage_WalletDetail(
                uistate = uistate,
                viewModel = viewModel,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Profilepage_Security(
                viewModel = viewModel,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Profilepage_Preferences(modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.padding(16.dp))

            Button(
                onClick = { viewModel.logout() },
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
    uistate: ProfileUiState,
    viewModel: ProfileViewModel
) {
    var showEditProfileDialog by remember { mutableStateOf(false) }

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
            Text(text = uistate.name, fontSize = 20.sp)
            Spacer(
                modifier = Modifier.padding(4.dp)
            )
            Text(text = uistate.email, fontSize = 16.sp)
            Spacer(
                modifier = Modifier.padding(4.dp)
            )
            Button(
                onClick = { showEditProfileDialog = true },
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
        if (showEditProfileDialog) {
            EditProfileDialog(
                currentName = uistate.name,
                currentPhone = uistate.phone,
                onConfirm = { name, phone ->
                    viewModel.updateProfile(name, phone)
                    showEditProfileDialog = false
                },
                onDismiss = { showEditProfileDialog = false }
            )
        }
    }
}

@Composable
fun Profilepage_AccountDetail(
    username: String,
    email: String,
    phone: String,
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
                modifier = Modifier.padding(12.dp)
            )
            Text(text = stringResource(R.string.fullname), fontWeight = FontWeight.Bold)
            Spacer(
                modifier = Modifier.padding(2.dp)
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
                modifier = Modifier.padding(8.dp)
            )
            Text(text = stringResource(R.string.email), fontWeight = FontWeight.Bold)
            Spacer(
                modifier = Modifier.padding(2.dp)
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
                modifier = Modifier.padding(8.dp)
            )
            Text(text = stringResource(R.string.phone_num), fontWeight = FontWeight.Bold)
            Spacer(
                modifier = Modifier.padding(2.dp)
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
    uistate: ProfileUiState,
    viewModel: ProfileViewModel,
    modifier: Modifier = Modifier
) {
    var showEditBudgetDialog by remember { mutableStateOf(false) }

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
            Spacer(modifier = Modifier.padding(12.dp))
            Text(text = stringResource(R.string.currency), fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.padding(2.dp))
            Card(
                modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = uistate.currency)
                }
            }
            Spacer(
                modifier = Modifier.padding(8.dp)
            )
            Text(text = stringResource(R.string.cash), fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.padding(2.dp))
            Card(
                modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE6F7E6)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = uistate.cash.toString())
                }
            }
            Spacer(
                modifier = Modifier.padding(8.dp)
            )
            Text(text = stringResource(R.string.bank), fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.padding(2.dp))
            Card(
                modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE6F0FF)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = uistate.bank.toString())
                }
            }
            Spacer(
                modifier = Modifier.padding(8.dp)
            )
            Text(text = stringResource(R.string.ewallet), fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.padding(2.dp))
            Card(
                modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF5E6)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = uistate.ewallet.toString())
                }
            }

            Spacer(modifier = Modifier.padding(4.dp))

            Button(
                onClick = { showEditBudgetDialog = true },
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

    if (showEditBudgetDialog) {
        var cashText by remember { mutableStateOf(uistate.cash.toString()) }
        var bankText by remember { mutableStateOf(uistate.bank.toString()) }
        var ewalletText by remember { mutableStateOf(uistate.ewallet.toString()) }

        AlertDialog(
            onDismissRequest = { showEditBudgetDialog = false },
            title = { Text(text = "Edit Budget") },
            text = {
                Column {
                    OutlinedTextField(
                        value = cashText,
                        onValueChange = { cashText = it },
                        label = { Text("Cash") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = bankText,
                        onValueChange = { bankText = it },
                        label = { Text("Bank") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = ewalletText,
                        onValueChange = { ewalletText = it },
                        label = { Text("Ewallet") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val cash = cashText.toDoubleOrNull() ?: uistate.cash
                        val bank = bankText.toDoubleOrNull() ?: uistate.bank
                        val ewallet = ewalletText.toDoubleOrNull() ?: uistate.ewallet

                        viewModel.updateBudget(cash, bank, ewallet)

                        showEditBudgetDialog = false
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                Button(onClick = { showEditBudgetDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}


@Composable
fun Profilepage_Security(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel
) {
    var showChangeEmailDialog by remember { mutableStateOf(false) }
    var showChangePasswordDialog by remember { mutableStateOf(false) }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = stringResource(R.string.security), fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = { showChangePasswordDialog = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0x80FFFFFF),
                    contentColor = Color.Black
                ),
                border = BorderStroke(0.5.dp, Color.Gray)
            ) {
                Text(text = stringResource(R.string.change_password))
            }

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = { showChangeEmailDialog = true },
                modifier = Modifier.fillMaxWidth(),
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

    if (showChangePasswordDialog) {
        ChangePasswordDialog(
            onConfirm = { oldPassword, newPassword ->
                viewModel.changePassword(newPassword)
                showChangePasswordDialog = false
            },
            onDismiss = { showChangePasswordDialog = false }
        )
    }

    if (showChangeEmailDialog) {
        ChangeEmailDialog(
            onConfirm = { currentPassword, newEmail ->
                viewModel.changeEmail(currentPassword, newEmail)
                showChangeEmailDialog = false
            },
            onDismiss = { showChangeEmailDialog = false }
        )
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
            Text(text = stringResource(R.string.preferences), fontWeight = FontWeight.Bold)
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
                    Text(text = stringResource(R.string.push_notigication_intro), fontSize = 14.sp, color = Color(0xFF424242))
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
                    Text(text = Language, fontSize = 14.sp, color = Color(0xFF424242))
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
        ProfilePage(
            userId = "user_id",
            viewModel = viewModel()
        )
    }
}