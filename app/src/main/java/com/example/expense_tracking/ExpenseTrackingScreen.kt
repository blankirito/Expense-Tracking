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
import com.example.expense_tracking.ui.ExpenseTrackingViewModel

enum class AppScreen {
    AUTHENTICATION,
    MAIN
}

enum class ExpenseTrackingScreen {
    Home,
    Add,
    Scan,
    Budget,
    Profile
}

@Composable
fun ExpenseTrackingApplication() {
    AppRoot(
        viewModel = ExpenseTrackingViewModel()
    )
}

@Composable
fun AppRoot(viewModel: ExpenseTrackingViewModel) {
    var showLogin by remember { mutableStateOf(true) }

    if (!viewModel.isLoggedIn) {
        if (showLogin) {
            LoginPage(
                viewModel = viewModel,
                onLoginSuccess = { viewModel.isLoggedIn = true },
                onRegisterClick = { showLogin = false }
            )
        } else {
            RegisterPage(
                viewModel = viewModel,
                onRegisterSuccess = { viewModel.isLoggedIn = true },
                onLoginClick = { showLogin = true }
            )
        }
    } else {
        MainScaffold(viewModel)
    }
}


@Composable
fun MainScaffold(viewModel: ExpenseTrackingViewModel) {
    var currentScreen by remember { mutableStateOf(ExpenseTrackingScreen.Home) }

    val navItems = listOf(
        Triple(ExpenseTrackingScreen.Home, R.drawable.home_24dp_e3e3e3_fill0_wght400_grad0_opsz24, "Home"),
        Triple(ExpenseTrackingScreen.Add, R.drawable.add_circle_24dp_e3e3e3_fill0_wght400_grad0_opsz24, "Add"),
        Triple(ExpenseTrackingScreen.Scan, R.drawable.photo_camera_24dp_e3e3e3_fill0_wght400_grad0_opsz24, "Scan"),
        Triple(ExpenseTrackingScreen.Budget, R.drawable.account_balance_wallet_24dp_e3e3e3_fill0_wght400_grad0_opsz24, "Budget"),
        Triple(ExpenseTrackingScreen.Profile, R.drawable.person_24dp_e3e3e3_fill0_wght400_grad0_opsz24, "Profile")
    )

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
            ExpenseTrackingScreen.Home -> HomePage(modifier)
            ExpenseTrackingScreen.Add -> AddPage(modifier)
            ExpenseTrackingScreen.Scan -> ScanPage(modifier)
            ExpenseTrackingScreen.Budget -> BudgetPage(modifier)
            ExpenseTrackingScreen.Profile -> ProfilePage(viewModel = viewModel)
        }
    }
}

