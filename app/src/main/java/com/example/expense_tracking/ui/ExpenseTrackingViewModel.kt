
package com.example.expense_tracking.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ExpenseTrackingViewModel: ViewModel() {

    var isLoggedIn by mutableStateOf(false)

    var login_password by mutableStateOf("")
    var login_email by mutableStateOf("")
    var loginSucess by mutableStateOf(false)

    fun login() {
        if (login_email.isNotBlank() && login_password.isNotBlank()) {
            loginSucess = true
        }
    }

    fun clearLoginState() {
        loginSucess = false
    }

    var register_fullname by mutableStateOf("")
    var register_email by mutableStateOf("")
    var register_password by mutableStateOf("")
    var confirm_password by mutableStateOf("")
    var registerSucess by mutableStateOf(false)

    fun register() {
        if (register_fullname.isNotBlank() && register_email.isNotBlank() && register_password.isNotBlank() && register_password == confirm_password) {
            registerSucess = true
        }
    }
    fun clearRegisterState() {
        registerSucess = false
    }

    fun logout() {
        login_email = ""
        login_password = ""
        loginSucess = false

        register_fullname = ""
        register_email = ""
        register_password = ""
        confirm_password = ""
        registerSucess = false

        isLoggedIn = false
    }
}