package com.example.expense_tracking.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.expense_tracking.data.RegisterRepository

class RegisterViewModel {

    var registerFullname by mutableStateOf("")
    var registerEmail by mutableStateOf("")
    var registerPassword by mutableStateOf("")
    var confirmPassword by mutableStateOf("")
    var registerError by mutableStateOf("")
    var registerSuccess by mutableStateOf(false)

    private val repo = RegisterRepository()

    fun register(onComplete: ((Boolean) -> Unit)? = null) {
        if (registerFullname.isBlank() || registerEmail.isBlank() || registerPassword.isBlank() || confirmPassword.isBlank()) {
            registerError = "Please fill in all fields"
            onComplete?.invoke(false)
            return
        }

        if (registerPassword != confirmPassword) {
            registerError = "Passwords do not match"
            onComplete?.invoke(false)
            return
        }

        repo.registerUser(name = registerFullname, email = registerEmail, password = registerPassword, "") { success, errorMsg ->
            if (success) {
                registerError = ""
                registerSuccess = true
                onComplete?.invoke(true)
            } else {
                registerError = errorMsg ?: "Registration failed"
                registerSuccess = false
                onComplete?.invoke(false)
            }
        }
    }
}