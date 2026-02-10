package com.example.expense_tracking.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.expense_tracking.data.Repository.LoginRepository

class LoginViewModel : ViewModel() {
    private val repository = LoginRepository()

    var isLoggedIn by mutableStateOf(false)

    var loginEmail by mutableStateOf("")
    var loginPassword by mutableStateOf("")
    var loginSuccess by mutableStateOf(false)
    var loginError by mutableStateOf("")

    fun login(onComplete: (Boolean) -> Unit) {
        repository.login_email = loginEmail
        repository.login_password = loginPassword

        repository.login { success ->
            loginSuccess = repository.loginSuccess
            loginError = repository.loginError
            onComplete(success)
        }
    }
}