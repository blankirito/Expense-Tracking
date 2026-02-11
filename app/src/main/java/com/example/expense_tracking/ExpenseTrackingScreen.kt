package com.example.expense_tracking.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.expense_tracking.ui.*
import com.example.expense_tracking.R
import com.example.expense_tracking.viewmodel.LoginViewModel
import com.example.expense_tracking.viewmodel.RegisterViewModel
import com.google.firebase.auth.FirebaseAuth

enum class ExpenseTrackingScreen {
    Home,
    Add,
    Scan,
    Budget,
    Profile
}

@Composable
fun ExpenseTrackingApplication() {
    AppEntryPoint()
}

@Composable
fun AppEntryPoint() {
    val auth = FirebaseAuth.getInstance()
    var isLoggedIn by remember { mutableStateOf(auth.currentUser != null) }

    if (isLoggedIn) {
        MainScaffold(currentUserId = auth.currentUser?.uid)
    } else {
        LoginRegisterWrapper(
            onLoginSuccess = { isLoggedIn = true }
        )
    }
}

@Composable
fun LoginRegisterWrapper(
    onLoginSuccess: () -> Unit
) {
    var showLogin by remember { mutableStateOf(true) }

    val loginViewModel = LoginViewModel()
    val registerViewModel = RegisterViewModel()

    if (showLogin) {
        LoginPage(
            viewModel = loginViewModel,
            onLoginSuccess = {
                loginViewModel.isLoggedIn = true
                onLoginSuccess()
            },
            onRegisterClick = { showLogin = false }
        )
    } else {
        RegisterPage(
            viewModel = registerViewModel,
            onRegisterSuccess = {
                loginViewModel.isLoggedIn = true
                onLoginSuccess()
            },
            onLoginClick = { showLogin = true }
        )
    }
}



@Composable
fun AppRoot(
    loginViewModel: LoginViewModel,
    registerViewModel: RegisterViewModel
    ) {
    var showLogin by remember { mutableStateOf(true) }

    var currentUserId by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser?.uid) }

    LaunchedEffect(loginViewModel.isLoggedIn) {
        if (loginViewModel.isLoggedIn) {
            currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        }
    }

    if (!loginViewModel.isLoggedIn) {
        if (showLogin) {
            LoginPage(
                viewModel = loginViewModel,
                onLoginSuccess = { loginViewModel.isLoggedIn = true },
                onRegisterClick = { showLogin = false }
            )
        } else {
            RegisterPage(
                viewModel = registerViewModel,
                onRegisterSuccess = { loginViewModel.isLoggedIn = true },
                onLoginClick = { showLogin = true }
            )
        }
    } else {
        MainScaffold(currentUserId)
    }
}


@Composable
fun MainScaffold(currentUserId: String?) {
    var currentScreen by remember { mutableStateOf(ExpenseTrackingScreen.Home) }

    val navItems = listOf(
        Triple(ExpenseTrackingScreen.Home, R.drawable.home_24dp_e3e3e3_fill0_wght400_grad0_opsz24, "Home"),
        Triple(ExpenseTrackingScreen.Add, R.drawable.add_circle_24dp_e3e3e3_fill0_wght400_grad0_opsz24, "Add"),
        Triple(ExpenseTrackingScreen.Scan, R.drawable.photo_camera_24dp_e3e3e3_fill0_wght400_grad0_opsz24, "Scan"),
        Triple(ExpenseTrackingScreen.Budget, R.drawable.account_balance_wallet_24dp_e3e3e3_fill0_wght400_grad0_opsz24, "Budget"),
        Triple(ExpenseTrackingScreen.Profile, R.drawable.person_24dp_e3e3e3_fill0_wght400_grad0_opsz24, "Profile")
    )

    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                navItems.forEach { (screen, iconRes, labelText) ->
                    NavigationBarItem(
                        selected = currentScreen == screen,
                        onClick = { currentScreen = screen },
                        icon = {
                            Icon(
                                painter = painterResource(id = iconRes),
                                contentDescription = labelText,
                            )
                        },
                        label = {
                            Text(
                                labelText
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            unselectedIconColor = Color.Gray,
                            selectedTextColor = Color.Black,
                            unselectedTextColor = Color.Gray
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)

        when (currentScreen) {
            ExpenseTrackingScreen.Home -> {
                if (currentUserId != null) {
                    HomePage(userId = currentUserId)
                } else {
                    Text("Loading user info...")
                }
            }
            ExpenseTrackingScreen.Add -> AddPage(modifier)
            ExpenseTrackingScreen.Scan -> ScanPage(modifier)
            ExpenseTrackingScreen.Budget -> {
                if (currentUserId != null) {
                    BudgetPage(userId = currentUserId)
                } else {
                    Text("Loading user info...")
                }
            }
            ExpenseTrackingScreen.Profile -> {
                if (currentUserId != null) {
                    ProfilePage(userId = currentUserId)
                } else {
                    Text("Loading user info...")
                }
            }
        }
    }
}

